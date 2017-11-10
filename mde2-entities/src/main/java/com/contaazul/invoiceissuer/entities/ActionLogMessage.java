package com.contaazul.invoiceissuer.entities;

import static com.contaazul.invoiceissuer.entities.ActionLogLevel.ERROR;
import static com.contaazul.invoiceissuer.entities.ActionLogLevel.INFO;
import static com.contaazul.invoiceissuer.entities.ActionLogLevel.SUCCESS;
import static com.contaazul.invoiceissuer.entities.ActionLogLevel.WARN;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionLogMessage {
	START(INFO, "Iniciando processamento."),
	A3_START(INFO, "Iniciando processamento (A3)."),
	A3_GENERATING(INFO, "Gerando XML da nota (A3)."),
	A3_GENERATION_FAIL(WARN, "Erro ao gerar XML da nota (A3)."),
	UNEXPECTED_ERROR(ERROR, "Erro desconhecido."),
	ADDED_TO_QUERY_QUEUE(INFO, "Nota aguardando retorno da SEFAZ."),
	REJECTED(WARN, "Nota rejeitada pela SEFAZ."),
	REPEATED_REJECTION(WARN, "Nota rejeitada anteriormente pela SEFAZ."),
	QUERYING(INFO, "Buscando retorno da nota na SEFAZ."),
	STILL_PROCESSING(INFO, "Nota ainda em processamento na SEFAZ."),
	ISSUED(SUCCESS, "Nota emitida com sucesso."),
	FAIL(WARN, "Falha."),
	SENDING_EMAIL(INFO, "Enviando nota por email."),
	DUPLICATE(SUCCESS, "Nota retornaria duplicidade, mas foi ajustada para retornar o sucesso da anterior."),
	FORCED_SUCCESS(SUCCESS, "Status de sucesso retornado manualmente."),
	IMPROPER_CONSUMPTION(WARN, "Consumo indevido."),
	BATCH_NOT_FOUND(WARN, "Lote não encontrado."),
	TIMED_OUT(WARN, "Tempo esgotado na comunicação com a SEFAZ."),
	UNEXPECTED_QUERY_ERROR(WARN, "Ocorreu um erro desconhecido ao buscar o retorno com a SEFAZ. "),
	GNRE_REQUESTED(INFO, "Requisição de GNRE solicitada."),
	GNRE_START(INFO, "Iniciando processamento de GNRE."),
	GNRE_SUCCESS(INFO, "GNRE processado com sucesso."),
	GNRE_REJECTED(WARN, "GNRE rejeitada."),
	GNRE_FAIL(WARN, "Falha ao processar GNRE."),
	MDE_START(INFO, "Iniciado processamento do evento."),
	MDE_SUCCESS(INFO, "Evento processado com sucesso."),
	MDE_FAIL(WARN, "Falha ao processar evento.");

	private final ActionLogLevel level;
	private final String text;
}
