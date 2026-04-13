package scripts_backup;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

import sistema.model.Dependente;

public class CalculoTesteData {

    public Integer diferencaAno(String dataAniversario) {

        LocalDate data = converterData(dataAniversario).toLocalDate();

        LocalDate and = LocalDate.now();

        Period diff = Period.between(data, and);
        int idade = diff.getYears();

        return idade;
    }

    public Date converterData(String valor) {
        Date data = Date.valueOf(valor);
        return data;

    }

}
