package com.example.learnenglish.controllers;

import com.example.learnenglish.service.UserService;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ImageController {
    private final ServletContext servletContext;
    private final ResourceLoader resourceLoader;

    @Value("${upload.path}")
    private String uploadPath;
    private final UserService userService;

    public ImageController(ServletContext servletContext, ResourceLoader resourceLoader, UserService userService) {
        this.servletContext = servletContext;
        this.resourceLoader = resourceLoader;
        this.userService = userService;
    }
//    private final ImageRepository imageRepository;

//    @GetMapping("/images/{id}")
//    private ResponseEntity<?> getImageById(@PathVariable Long id) {
//        Image image = imageRepository.findById(id).orElse(null);
//        return ResponseEntity.ok()
//                .header("fileName", image.getOriginalFileName())
//                .contentType(MediaType.valueOf(image.getContentType()))
//                .contentLength(image.getSize())
//                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
//    }

    //    @PostMapping("/user/{userId}/upload-avatar")
    @RequestMapping("/upload")
    public ResponseEntity<String> userUploadAvatar(@RequestParam(required = false, value = "file")
                                                   MultipartFile file, Principal principal) throws IOException {
        if (principal != null && file != null) {
            System.out.println(" *******************************************************");
            Long userId = userService.findByEmail(principal.getName()).getId();
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            Resource resource = resourceLoader.getResource("file:" + (uploadPath + "/" + resultFilename));
            String relativePath = "";
            try {
                relativePath = resource.getURL().getPath();
                relativePath = relativePath.replace("/home/anatolii/Documents/learnEnglishImages", "");
            } catch (IOException e) {
                e.printStackTrace();
            }
//            return relativePath;
            userService.saveUserAvatar(userId, relativePath);
//            String realPath = servletContext.getRealPath("/");
//            Path absolute = Paths.get(uploadPath + "/" + resultFilename);
//            Path base = Paths.get(realPath);
//            Path relative = base.relativize(absolute);
////            "/" + relative.toString().replace('\\', '/');
//            userService.saveUserAvatar(userId, "/" + relative.toString().replace('\\', '/'));

        }
        return ResponseEntity.ok("filetest");
    }






//
//        @GetMapping("/")
//        public String listUploadedFiles(Model model) throws IOException {
//
//            model.addAttribute("files", storageService.loadAll().map(
//                            path -> MvcUriComponentsBuilder.fromMethodName(ImageController.class,
//                                    "serveFile", path.getFileName().toString()).build().toUri().toString())
//                    .collect(Collectors.toList()));
//
//            return "uploadForm";
//        }
//
//        @GetMapping("/files/{filename:.+}")
//        @ResponseBody
//        public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
//
//            Resource file = storageService.loadAsResource(filename);
//            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//        }
//
//        @PostMapping("/upload")
//        public String handleFileUpload(@RequestParam("file") MultipartFile file,
//                                       RedirectAttributes redirectAttributes) {
//
//            storageService.store(file);
//            redirectAttributes.addFlashAttribute("message",
//                    "You successfully uploaded " + file.getOriginalFilename() + "!");
//
//            return "redirect:/";
//        }


}
