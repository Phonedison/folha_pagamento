package sistema.service.io;

import java.util.ArrayList;
import java.util.List;

public class ImportacaoResultado {

    public int funcionarioImportados = 0;
    public int dependentesImportados = 0;

    public int funcionariosDuplicados = 0;
    public int dependentesDuplicados = 0;

    public List<String> funcionariosDuplicadosLista = new ArrayList<>();
    public List<String> dependentesDuplicadosLista = new ArrayList<>();

}
