package org.example.backend.controller;

import org.example.backend.model.Summary;
import org.example.backend.model.assemblyai.AssemblyAiResponse;
import org.example.backend.model.assemblyai.FileUploadRequest;
import org.example.backend.service.AssemblyAiUploadService;
import org.example.backend.service.AssemblyAiTranscriptService;
import org.example.backend.service.SummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class BackendController {

    private final AssemblyAiUploadService assemblyAiUploadService;
    private final AssemblyAiTranscriptService transcriptService;
    private final SummaryService service;

    public BackendController(AssemblyAiUploadService assemblyAiUploadService, AssemblyAiTranscriptService transcriptService, SummaryService service) {
        this.assemblyAiUploadService = assemblyAiUploadService;
        this.transcriptService = transcriptService;
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
            // transfer MultipartFile in Record
            FileUploadRequest fileUploadRequest = new FileUploadRequest(
                    file.getOriginalFilename(),
                    file.getBytes()
            );
            // Upload the file on AssemblyAi
            AssemblyAiResponse assemblyAiResponse = assemblyAiUploadService.uploadFile(fileUploadRequest);
            // transcribe the audio
            String transcript = transcriptService.transcriptFile(assemblyAiResponse).orElse("Fehler");
            // summarize the transcript
            Summary summary = service.createSummary(transcript);

        return ResponseEntity.ok(summary.id());
    }

}