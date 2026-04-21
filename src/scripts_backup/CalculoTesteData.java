package scripts_backup;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

public class CalculoTesteData {
    // Método que calcula a diferença em anos (idade) a partir da data de
    // nascimento.
    public Integer diferencaAno(String dataAniversario) {
        // Converte a String recebida (ex: "2000-05-10") em Date e depois em LocalDate.
        LocalDate data = converterData(dataAniversario).toLocalDate();
        // Pega a data atual do sistema (hoje)
        LocalDate and = LocalDate.now();
        // Calcula a diferença entre as duas datas (data de nascimento e hoje).
        Period diff = Period.between(data, and);
        // Pega apenas os anos da diferença (idade em anos completos).
        int idade = diff.getYears();
        // Retorna a idade calculada.
        return idade;
    }

    // Método responsável por converter uma String em Date
    public Date converterData(String valor) {
        // Converte a String no formato "yyyy-MM-dd" para um Date do SQL
        Date data = Date.valueOf(valor);
        // Retorna a data convertida
        return data;

    }

}
