package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.serializer.ExportFormat;
import by.pavel.imageannotationbe.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("projects/{projectId}/export")
public class ExportController {

    private final ExportService exportService;

    @PostMapping(value = "/csv", produces = "application/zip")
    public void exportDataCsv(
            HttpServletResponse response,
            @PathVariable Long projectId,
            @RequestBody Set<UUID> imageIds) throws IOException {
        response.addHeader("Content-Disposition", "attachment; filename=\"csv.zip\"");
        exportService.exportData(imageIds, projectId, response.getOutputStream(), ExportFormat.CSV);
    }

    @PostMapping(value = "/json", produces = "application/zip")
    public void exportDataJson(
            HttpServletResponse response,
            @PathVariable Long projectId,
            @RequestBody Set<UUID> imageIds
    ) throws IOException {
        response.addHeader("Content-Disposition", "attachment; filename=\"json.zip\"");
        exportService.exportData(imageIds, projectId, response.getOutputStream(), ExportFormat.JSON);
    }
}
