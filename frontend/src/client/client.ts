import {AxiosInstance} from 'axios';
import {Application, ApplicationLog, BatchPage, Configuration, SessionStatement, SessionStatementCode, SessionStatementPage} from './types';

export class Api {
  client: AxiosInstance;

  constructor(client: AxiosInstance) {
    this.client = client;
  }

  private get(url: string) {
    return this.client.get(url).then((resp) => resp.data);
  }

  private delete(url: string) {
    return this.client.delete(url);
  }

  fetchBatches(size: number, from: number, status?: string | null): Promise<BatchPage> {
    return this.get(`/api/batches?size=${size}&from=${from}${status ? '&state=' + status : ''}`);
  }

  fetchBatch(id: string): Promise<Application> {
    return this.get(`/api/batches/${id}`);
  }

  deleteBatch(id: string) {
    return this.delete(`/api/batches/${id}`);
  }

  fetchBatchLog(id: string): Promise<ApplicationLog> {
    return this.get(`/api/batches/${id}/log`);
  }

  fetchSessions(size: number, from: number): Promise<BatchPage> {
    return this.get(`/api/sessions?size=${size}&from=${from}`);
  }

  fetchSession(id: string): Promise<Application> {
    return this.get(`/api/sessions/${id}`);
  }

  deleteSession(id: string) {
    return this.delete(`/api/sessions/${id}`);
  }

  fetchSessionLog(id: string): Promise<ApplicationLog> {
    return this.get(`/api/sessions/${id}/log`);
  }

  fetchConfiguration(): Promise<Configuration> {
    return this.get('/api/configuration');
  }

  fetchSessionStatements(sessionId: string, size: number, from: number): Promise<SessionStatementPage> {
    return this.get(`/api/sessions/${sessionId}/statements?size=${size}&from=${from}`);
  }

  postSessionStatement(sessionId: string, statement: SessionStatementCode): Promise<SessionStatement> {
    return this.client.post(`/api/sessions/${sessionId}/statements`, statement);
  }

  cancelSessionStatement(sessionId: string, statementId: string): Promise<void> {
    return this.client.post(`/api/sessions/${sessionId}/statements/${statementId}/cancel`);
  }
}
