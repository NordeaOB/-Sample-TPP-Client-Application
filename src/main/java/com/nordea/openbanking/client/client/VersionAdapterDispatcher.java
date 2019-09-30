package com.nordea.openbanking.client.client;

import com.nordea.openbanking.client.session.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VersionAdapterDispatcher {

    private final List<VersionAdapter> versionAdapters;
    private final UserSession userSession;

    @SuppressWarnings("unchecked")
    public <T> T getAdapterFor(Class<T> adapterClass) {
        return versionAdapters.stream()
                .filter(adapter -> userSession.getApiVersion().equalsIgnoreCase(adapter.getVersion())
                        && adapterClass.isAssignableFrom(adapter.getClass())
                )
                .findFirst()
                .map(adapter -> (T) adapter)
                .orElseThrow(() -> new IllegalStateException(
                        "Can't find adapter for version: " + userSession.getApiVersion()
                ));
    }

}
