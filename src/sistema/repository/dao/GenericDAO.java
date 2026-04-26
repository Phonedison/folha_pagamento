package sistema.repository.dao;

import java.util.List;

public interface GenericDAO<T> {
    void salvar(T entidade);

    void atualizar(T entidade, int id, int opcao);

    void excluir(int id);

    List<T> listarTodos(int opcao);
}
