package com.lucasmello.applistatarefas.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.lucasmello.applistatarefas.database.DataBaseHelper.Companion.COLUNA_DATA_CADASTRO
import com.lucasmello.applistatarefas.database.DataBaseHelper.Companion.COLUNA_DESCRICAO
import com.lucasmello.applistatarefas.database.DataBaseHelper.Companion.COLUNA_ID_TAREFA
import com.lucasmello.applistatarefas.database.DataBaseHelper.Companion.NOME_TABELA_TAREFAS
import com.lucasmello.applistatarefas.model.Tarefa


class TarefaDAO(context: Context) : ITarefaDAO {

    private val escrita = DataBaseHelper(context).writableDatabase
    private val leitura = DataBaseHelper(context).readableDatabase


    override fun salvar(tarefa: Tarefa): Boolean {

        val conteudos = ContentValues()
        conteudos.put("${DataBaseHelper.COLUNA_DESCRICAO}", tarefa.descricao)

        try {
            escrita.insert(
                DataBaseHelper.NOME_TABELA_TAREFAS,
                null,
                conteudos
            )
            Log.i("INFO_DB", "Sucesso ao salvar tarefa")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("INFO_DB", "Erro ao salvar tarefa")
            return false
        }
        return true
    }


    override fun atualizar(tarefa: Tarefa): Boolean {


        val args = arrayOf( tarefa.idTarefa.toString() )
        val conteudo = ContentValues()
        conteudo.put("${COLUNA_DESCRICAO}", tarefa.descricao )

        try {
            escrita.update(
                NOME_TABELA_TAREFAS,
                conteudo,
                "${COLUNA_ID_TAREFA} = ?",
                args
            )
            Log.i("info_db", "Sucesso ao atualizar tarefa")
        }catch (e: Exception){
            e.printStackTrace()
            Log.i("info_db", "Erro ao atualizar tarefa")
            return false
        }

        return true

    }

    override fun remover(idTarefa: Int): Boolean {

        val args = arrayOf( idTarefa.toString() )
        try {
            escrita.delete(
                NOME_TABELA_TAREFAS,
                "${COLUNA_ID_TAREFA} = ?",
                args
            )
            Log.i("info_db", "Sucesso ao remover tarefa")
        }catch (e: Exception){
            e.printStackTrace()
            Log.i("info_db", "Erro ao remover tarefa")
            return false
        }

        return true

    }

    override fun listar(): List<Tarefa> {
        val listaTarefas = mutableListOf<Tarefa>()

        val sql = "SELECT ${COLUNA_ID_TAREFA}, " +
                "${COLUNA_DESCRICAO}, " +
                "    strftime('%d/%m/%Y %H:%M', ${COLUNA_DATA_CADASTRO}) ${COLUNA_DATA_CADASTRO} " +
                "FROM ${NOME_TABELA_TAREFAS}"



        val cursor = leitura.rawQuery(sql, null)

        val indiceIdTarefa = cursor.getColumnIndex(DataBaseHelper.COLUNA_ID_TAREFA)
        val indiceDescricao = cursor.getColumnIndex(DataBaseHelper.COLUNA_DESCRICAO)
        val indiceData = cursor.getColumnIndex(DataBaseHelper.COLUNA_DATA_CADASTRO)

        while (cursor.moveToNext()) {
            val idTarefa = cursor.getInt(indiceIdTarefa)
            val descricao = cursor.getString(indiceDescricao)
            val data = cursor.getString(indiceData)

            listaTarefas.add(
                Tarefa( idTarefa, descricao, data ) )
        }
        return listaTarefas
    }
}