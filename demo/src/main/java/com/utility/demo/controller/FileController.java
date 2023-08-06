package com.utility.demo.controller;

import com.utility.demo.model.DecoderForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileOutputStream;
import java.io.IOException;

@Controller
public class FileController {

    @GetMapping("/")
    public String index() {
        return "index"; // This corresponds to the name of the HTML file without the .html extension
    }
    @GetMapping("/decodeBase64")
    public String indexV2() {
        return "decoder"; // This corresponds to the name of the HTML file without the .html extension
    }

    @GetMapping("/ecodeBase64-v2")
    public String indexV21() {
        return "decoder"; // This corresponds to the name of the HTML file without the .html extension
    }

    @PostMapping("/decode")
    public @ResponseBody String decodeFile(@RequestBody String base64EncodedData, @RequestHeader String  fileName) {
        try {
            char targetChar = '\"';
            byte[] decodedBytes = Base64Utils.decodeFromString(base64EncodedData.replace(Character.toString(targetChar), "").replace(Character.toString(targetChar), "").replace(Character.toString(targetChar), ""));
            // Process the decoded bytes here, if required.

            // For simplicity, we return the base64 decoded content as a string.
            return new String(decodedBytes);
        } catch (IllegalArgumentException e) {
            return "Error decoding the file.";
        }
    }

    @PostMapping("/encode")
    public String encodeToBase64(@RequestParam("file") MultipartFile file, Model model) {
        try {
            byte[] fileContent = file.getBytes();
            String encodedBase64String = Base64Utils.encodeToString(fileContent);
            model.addAttribute("encodedBase64String", encodedBase64String);
        } catch (IOException e) {
            model.addAttribute("error", "Error reading the file: " + e.getMessage());
        }
        return "index";
    }

    @PostMapping("/")
    public String decodeAndDownload(@ModelAttribute("decoderForm") DecoderForm decoderForm,
                                    RedirectAttributes redirectAttributes) {
        try {
            byte[] decodedBytes = Base64Utils.decodeFromString(decoderForm.getBase64String());

            // Determine the file type based on the first few bytes of the decoded data.
            // Replace "application/octet-stream" with the appropriate MIME type for your specific use case.
            String mimeType = "application/octet-stream";

            // Use the provided file name, or use a default name if it's not provided
            String fileName = decoderForm.getFileName().isEmpty() ? "decoded_file" : decoderForm.getFileName();

            // Save the decoded bytes as a file
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                fos.write(decodedBytes);
            }

            redirectAttributes.addFlashAttribute("successMessage", "File decoded and downloaded successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error decoding the file: " + e.getMessage());
        }
        return "redirect:/";
    }
}