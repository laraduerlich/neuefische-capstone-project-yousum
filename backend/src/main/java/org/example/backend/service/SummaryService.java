package org.example.backend.service;

import com.assemblyai.api.resources.transcripts.types.Transcript;
import org.example.backend.model.Summary;
import org.example.backend.repo.SummaryRepo;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {

    private final SummaryRepo repo;
    private final IdService idService;
    private final ChatGPTService chatGPTService;


    public SummaryService(SummaryRepo repo, IdService idService, ChatGPTService chatGPTService) {
        this.repo = repo;
        this.idService = idService;
        this.chatGPTService = chatGPTService;
    }

    public Summary createSummary(String transcript) {
        Summary newSummary = new Summary (
                idService.generateId(),
                "title",
                chatGPTService.summarizeTranscript(transcript)
        );
        return repo.save(newSummary);
    }
}
