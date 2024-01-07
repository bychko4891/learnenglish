package com.example.learnenglish.controllers.restConrollers;

import com.example.learnenglish.dto.FieldErrorDTO;
import com.example.learnenglish.dto.PhraseUserDto;
import com.example.learnenglish.mapper.PhraseUserMapper;
import com.example.learnenglish.model.users.PhraseUser;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.service.PhraseUserService;
import com.example.learnenglish.service.UserService;
import com.example.learnenglish.validate.ValidatePhraseUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PhraseUserRestController {

    private final PhraseUserService service;

    private final UserService userService;



    @PostMapping("/phrase/add")
    public ResponseEntity<?> addNewPhraseUser(@Valid @RequestBody PhraseUserDto phraseUserDto,
                                                BindingResult bindingResult,
                                                Principal principal) {
        if (principal != null) {
            if (bindingResult.hasErrors()) {
                List<FieldErrorDTO> errors = bindingResult.getFieldErrors().stream()
                        .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            service.cleaningText(phraseUserDto);
            if(ValidatePhraseUser.validateTranslationPairs(phraseUserDto)){
                User user = userService.findByEmail(principal.getName());
                if(service.duplicatePhraseUserInDB(user, phraseUserDto.getEngPhrase())) return ResponseEntity.ok(new CustomResponseMessage(Message.LOGIN_ERROR)); // ЗМІНИТИ !!!
                return ResponseEntity.ok(service.saveNewPhraseUser(user, phraseUserDto));
            }
            return ResponseEntity.ok(new CustomResponseMessage(Message.LOGIN_ERROR)); // ЗМІНИТИ!!!!!!!!!!!
        }
        return ResponseEntity.ok(new CustomResponseMessage(Message.LOGIN_ERROR));
    }

}