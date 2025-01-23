export interface PessoaDto {
    id: string;
    nome: string;
    cpf: string;
    telefone: string;
    numero: string;
    complemento: string | null;
    cep: string;
    bairro: string;
    municipio: string;
    estado: string;
    createdBy: string | undefined;
    lastModificationBy: string | undefined;
    createdAt: string;
    lastModificationAt: string;
  }