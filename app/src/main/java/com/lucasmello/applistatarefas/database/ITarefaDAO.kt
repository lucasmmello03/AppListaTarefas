package com.lucasmello.applistatarefas.database

import com.lucasmello.applistatarefas.model.Tarefa

interface ITarefaDAO {

    fun salvar(tarefa: Tarefa) : Boolean
    fun atualizar(tarefa: Tarefa) : Boolean
    fun remover(idTarefa: Int) : Boolean
    fun listar() : List<Tarefa>
}