package sistema.service;

import sistema.app.util.CustomLogger;
import sistema.model.FolhaPagamento;
import sistema.model.Funcionario;

public class FolhaService {
    public void processarFolhaCompleta(FolhaPagamento folha) {
        Funcionario funcionario = folha.getFuncionario();
        double salarioBruto = funcionario.getSalarioBruto();
        int numDependente = funcionario.getDependentes().size();
        double inss = calcularINSS(salarioBruto);
        double ir = calcularIR(salarioBruto, inss, numDependente);

        try {
            folha.setDescontoInss(inss);
            folha.setDescontoIR(ir);
        } catch (Exception e) {
            CustomLogger.logError("Erro ao processar Folha de pagamento!");
            throw new RuntimeException(e.getMessage());
        }
    }

    private double calcularINSS(double salario) {
        double inss, base = Math.min(salario, 8157.41);

        if (salario <= 1518.00) {
            inss = (base * 0.075);

        } else if (salario <= 2793.88) {
            inss = (base * 0.09) - 22.77;

        } else if (salario <= 4190.83) {
            inss = (base * 0.12) - 106.60;

        } else {
            inss = (base * 0.14) - 190.42;
        }
        return Math.max(0, Math.min(inss, 951.62));
    }

    public double calcularIR(double salario, double inss, int numDependentes) {
        double base = salario - inss - (numDependentes * 189.59);

        if (base <= 2259.20)
            return 0;

        if (base <= 2826.65)
            return (base * 0.075) - 169.44;

        if (base <= 3751.05)
            return (base * 0.15) - 381.44;

        if (base <= 4664.68)
            return (base * 0.225) - 662.77;

        return (base * 0.275) - 896.00;
    }
}
