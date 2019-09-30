package com.nordea.openbanking.client.resources;

import com.nordea.openbanking.client.model.auth.CompleteAuthorization;
import com.nordea.openbanking.client.model.auth.StartAuthorization;
import com.nordea.openbanking.client.service.auth.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/authorize")
@RequiredArgsConstructor
public class AuthorizationResource {

    private final AuthorizationService authorizationService;

    @GetMapping("/startAuthorization/{clientType}")
    public String get(Model model, @PathVariable("clientType") String clientType) {
        StartAuthorization startAuthorization = new StartAuthorization();
        startAuthorization.setClientType(clientType);
        model.addAttribute("environments", authorizationService.getEnvironments(clientType));
        model.addAttribute("startAuthorization", startAuthorization);
        return "startAuthorizationStep";
    }

    @PostMapping("/startAuthorization")
    public ModelAndView startAuthorization(@ModelAttribute StartAuthorization settings) {
        if (settings.getClientType().equals("corporate")){
            settings.setCountry("CORP");
        }
        authorizationService.updateSession(settings);
        ModelAndView modelAndView = new ModelAndView("completeAuthorizationStep");
        CompleteAuthorization completeAuthorization = new CompleteAuthorization();
        completeAuthorization.setCountry(settings.getCountry());
        modelAndView.addObject("completeAuthorization", completeAuthorization);
        return modelAndView;
    }
    
    @GetMapping("/completeAuthorization")
    public String completeAuthorization(@ModelAttribute CompleteAuthorization authorization, Model model) {
        return authorizationService.authorize(authorization, model);
    }

    @GetMapping("/oauth/callback")
    public String exchangeCodeToToken(@RequestParam Map params) {
        if (Optional.ofNullable(params).isPresent()) {
            Optional.ofNullable(params.get("code")).ifPresent(code -> authorizationService.exchangeCodeAndAuthorize((String) code));
            return "redirect:/accounts";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping("/decoupled/token")
    public ResponseEntity<?> authorizeDecoupledIfReady(@RequestParam ("orderRef") String orderRef,
                                                       @RequestParam ("tppToken") String tppToken) {
        if (authorizationService.authorizeDecoupledIfReady(orderRef, tppToken)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{accessId}")
    public ResponseEntity<?> authorizeCorporateIfStatusActive(@PathVariable("accessId") String accessId, HttpServletRequest request) {
        String clientToken = request.getHeader("Authorization");

        if (authorizationService.authorizeDecoupledIfReady(accessId, clientToken)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/waitForAuthorization")
    public String waitForAuthorization() {
        return "waitForAuthorization";
    }

    @GetMapping("/waitForCorpAuthorization")
    public String waitForCorpAuthorization() {
        return "waitForCorpAuthorization";
    }
    
}
