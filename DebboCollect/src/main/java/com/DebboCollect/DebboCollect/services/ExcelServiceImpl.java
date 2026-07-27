package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.entity.Collecte;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.entity.Reponse;
import com.DebboCollect.DebboCollect.repository.CollectRepository;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import com.DebboCollect.DebboCollect.repository.ReponseRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {

    private final ProjetRepository projetRepository;
    private final CollectRepository collectRepository;
    private final ReponseRepository reponseRepository;

    @Override
    public byte[] exporterProjetExcel(Long projetId) {

        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() ->
                        new RuntimeException("Projet introuvable"));

        List<Collecte> collectes =
                collectRepository.findByProjetId(projetId);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            XSSFSheet sheet = workbook.createSheet(projet.getNom());

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Collecte");
            header.createCell(1).setCellValue("Date");


            int colonne = 3;

            // Création des colonnes avec les questions
            for (var champ : projet.getChamps()) {

                header.createCell(colonne++)
                        .setCellValue(champ.getQuestion());
            }

            int rowIndex = 1;

            for (Collecte collecte : collectes) {

                Row row = sheet.createRow(rowIndex++);

                row.createCell(0)
                        .setCellValue(collecte.getNumeroCollecteProjet());

                row.createCell(1)
                        .setCellValue(collecte.getDateCollecte().toString());


                List<Reponse> reponses =
                        reponseRepository.findByCollecteId(
                                collecte.getId());

                int col = 3;

                for (var champ : projet.getChamps()) {

                    String valeur = "";

                    for (Reponse reponse : reponses) {

                        if (reponse.getChamp().getId()
                                .equals(champ.getId())) {

                            valeur = reponse.getValeur();
                            break;
                        }
                    }

                    row.createCell(col++).setCellValue(valeur);
                }
            }

            // Ajuster automatiquement la largeur des colonnes
            for (int i = 0; i < header.getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            workbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur lors de la génération du fichier Excel",
                    e
            );
        }
    }
}