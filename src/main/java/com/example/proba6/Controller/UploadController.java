package com.example.proba6.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.proba6.config.EncoderDecoder.decodeAudioData;

@Controller
@RequestMapping("/upload")
public class UploadController {

    @PostMapping
//    @ResponseBody
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile file, Model model) {
        int size = 0;
        String dm = "No decode";
        if (!file.isEmpty()) {
            try {
                // Пат до директориумот каде што сакате да зачувате фајловите
                String targetDirectory = "/home/kristina/Documents/verzija3_1/Neta/src/main/resources/static/Output/";
                String filepath = file.getOriginalFilename();

                // Зачувај го фајлот на серверот
                byte[] bytes = file.getBytes();
                java.nio.file.Path path = java.nio.file.Paths.get(targetDirectory + file.getOriginalFilename());
                java.nio.file.Files.write(path, bytes);


//                od tuka kje dekodirammm
                System.out.println(filepath);
                String outputpath = targetDirectory + filepath;
                System.out.println(outputpath);
                for (int i = 0; i < filepath.length(); i++) {
                    char c = filepath.charAt(i);
                    if (Character.isDigit(c)) {
                        int digitValue = Character.getNumericValue(c);
                        size = size * 10 + digitValue;
                        System.out.println("Goleminata " + size);
                    }
                }
                dm = decodeAudioData(outputpath, size);
                System.out.println("Dekodirana" + dm);
                System.out.println("Uspeshno.");
            } catch (Exception e) {
                System.out.println("Greska " + e.getMessage());
            }
        } else {
            System.out.println("Ne e izran fajl");
        }
        model.addAttribute("end", "The decoded message is");
        model.addAttribute("dm", dm);
        return "Success";
    }

//    @GetMapping("/done")
//    public String done()
//    {
//        return "Success";
//    }
//}
}

