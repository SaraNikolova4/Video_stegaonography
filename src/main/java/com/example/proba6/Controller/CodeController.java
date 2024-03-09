package com.example.proba6.Controller;

import com.example.proba6.Entity.Users;
import com.example.proba6.Service.UserService;
import com.example.proba6.config.CredentialRepository;
import com.example.proba6.config.ValidateCodeDto;
import com.example.proba6.config.Email;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/code")
public class CodeController {

    private final GoogleAuthenticator gAuth;
    private final CredentialRepository credentialRepository;
    private final UserService userService;
    private final Email emailSender;

    @GetMapping("/valid")
    public String getvalid(@RequestParam String username, Model model) {
        model.addAttribute("username", username);
        return "Validate";
    }

    @SneakyThrows
    @PostMapping("/generate")
    public String generate(@RequestParam String username,
                           HttpServletResponse response,
                           @RequestParam String code,
                           Model model) {
        System.out.println(emailSender.getcode());
        System.out.println(code);
        if(code.equals(emailSender.getcode())) {

            final GoogleAuthenticatorKey key = gAuth.createCredentials(username);
        //I've decided to generate QRCode on backend site
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("my-demo", username, key);

        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);

        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "inline; filename=qr-code.png");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
        String base64Image = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

        // Add the base64Image to the model to be used in the HTML template
        model.addAttribute("base64Image", base64Image);
        model.addAttribute("username", username);
        return "Code";
        }
        else
        {
            model.addAttribute("errorMessage", "Тhe codes do not match");
            model.addAttribute("username", username);
            return "Email";
        }

    }

    @PostMapping("/validate/key")
    public String validateKey(@ModelAttribute ValidateCodeDto form, Model model, HttpSession session) {
        String username = form.getUsername();

        int code = Integer.parseInt(form.getCode());
        // Добивање на податоците за корисникот од репозиториумот
        CredentialRepository.UserTOTP userTOTP = credentialRepository.getUser(username);

        if (userTOTP != null) {
            // Валидација на кодот со Google Authenticator
            boolean isValid = gAuth.authorizeUser(username, code);

            if (isValid) {
                Users users = (Users) session.getAttribute("account");
                if(users != null)
                {
                    model.addAttribute("user", users);
                    model.addAttribute("have", "Logout");
                }
                return "EncodeDecode";
            } else {
                model.addAttribute("validationResult", "Validation failed");
                return "Validate";
            }
        } else {
            model.addAttribute("validationResult", "User not found");
            return "Validate";
        }

    }



}