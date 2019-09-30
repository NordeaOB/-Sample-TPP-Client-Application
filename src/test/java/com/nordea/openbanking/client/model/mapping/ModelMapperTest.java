package com.nordea.openbanking.client.model.mapping;

import com.nordea.openbanking.v3.ais.model.AccountList;
import com.nordea.openbanking.v3.ais.model.AccountListResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ModelMapperTest {

    private ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setup() {
        PropertyMap<AccountListResponse, AccountList> accountListMap = new PropertyMap<AccountListResponse, AccountList>() {
            protected void configure() {
                map().links(source.getResponse().getLinks());
                map().accounts(source.getResponse().getAccounts());
            }
        };

        modelMapper.addMappings(accountListMap);
    }


    @Test
    public void accountListModelmapperTest() {
//        modelMapper.createTypeMap(AccountListResponse.class, AccountList.class);
        modelMapper.validate();
    }

}
