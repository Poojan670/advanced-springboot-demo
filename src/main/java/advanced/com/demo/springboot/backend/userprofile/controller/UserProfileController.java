package advanced.com.demo.springboot.backend.userprofile.controller;

import advanced.com.demo.springboot.backend.exception.CustomApiException;
import advanced.com.demo.springboot.backend.userprofile.DTO.UserProfileDTO;
import advanced.com.demo.springboot.backend.userprofile.model.UserProfile;
import advanced.com.demo.springboot.backend.userprofile.service.UserProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user-details-app")
public class UserProfileController {
    private final UserProfileServiceImpl userDetailsService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<Object> saveUserDetails(@Valid @RequestBody UserProfileDTO request) throws CustomApiException{
        return ResponseEntity.ok().body(userDetailsService.saveUserDetails(request));
    }

    @PatchMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<Object> updateUserDetails(
            String firstName, String middleName, String lastName,
            String address, LocalDate dateOfBirth, MultipartFile photo
    ) throws CustomApiException{
        return ResponseEntity.ok().body(userDetailsService.updateUserDetails(
                firstName, middleName, lastName,
                address, dateOfBirth, photo
        ));
    }

    @GetMapping("/user-details")
    public ResponseEntity<Page<UserProfile>> getUserDetails(
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "") String search
    ) {
        return ResponseEntity.ok().body(userDetailsService.getUserDetails(offset,limit,sortBy, search));
    }

    @GetMapping("/user-detail/{id}")
    public ResponseEntity<Object> getUserDetail(@Valid @PathVariable Long id){
        return ResponseEntity.ok().body(userDetailsService.getUserDetail(id));
    }

    @DeleteMapping("/user-detail/{id}")
    public ResponseEntity<Object> deleteUserDetail(@PathVariable Long id){
        userDetailsService.deleteUserDetail(id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
