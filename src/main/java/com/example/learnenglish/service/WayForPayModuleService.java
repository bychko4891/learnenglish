package com.example.learnenglish.service;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.WayForPayModule;
import com.example.learnenglish.repository.WayForPayModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Буде змінюватись
@Service
@RequiredArgsConstructor
public class WayForPayModuleService {

    private final WayForPayModuleRepository wayForPayModuleRepository;

    public void saveWayForPayModule(WayForPayModule wayForPayModule) {
        Optional<WayForPayModule> wayForPayModuleOptional = wayForPayModuleRepository.findById(wayForPayModule.getId());
        if (wayForPayModuleOptional.isEmpty()) {
            wayForPayModuleRepository.save(wayForPayModule);
        } else {
            WayForPayModule wayForPayModuleFromDataBase = wayForPayModuleOptional.get();
            wayForPayModuleFromDataBase.setActive(wayForPayModule.isActive());
            wayForPayModuleFromDataBase.setMerchantAccount(wayForPayModule.getMerchantAccount());
            wayForPayModuleFromDataBase.setMerchantSecretKey(wayForPayModule.getMerchantSecretKey());
            wayForPayModuleRepository.save(wayForPayModuleFromDataBase);
        }
    }

    public WayForPayModule getWayForPayModule() {
        Optional<WayForPayModule> wayForPayModuleOptional = wayForPayModuleRepository.findById(1l);
        if (wayForPayModuleOptional.isPresent()) {
            return wayForPayModuleOptional.get();
        } else return new WayForPayModule();
    }

}
