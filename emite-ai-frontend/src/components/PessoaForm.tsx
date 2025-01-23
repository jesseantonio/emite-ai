import React, { useState, useEffect } from 'react';
import { Button, TextField, Typography, Box, Snackbar, Alert } from '@mui/material';
import styled from 'styled-components';
import { useNavigate, useParams } from 'react-router-dom';
import InputMask from 'react-input-mask';
import { PessoaDto } from '../types/PessoaDto.tsx';
import api from '../services/api.tsx';

const FormBox = styled(Box)`
  display: flex;
  flex-direction: column;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  width: 500px;
`;

const LoginBox = styled(Box)`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  background-color: #f0f2f5;
`;

const PessoaForm: React.FC = () => {
  const { id } = useParams();
  const [formData, setFormData] = useState<PessoaDto>({
    id: '',
    nome: '',
    cpf: '',
    telefone: '',
    numero: '',
    complemento: null,
    cep: '',
    bairro: '',
    municipio: '',
    estado: '',
    createdBy: localStorage.getItem("username")?.replace(/"/g, ''),
    lastModificationBy: localStorage.getItem("username")?.replace(/"/g, ''),
    createdAt: '',
    lastModificationAt: '',
  });

  const [error, setError] = useState<string>('');
  const [loadingCep, setLoadingCep] = useState<boolean>(false);
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const [message, setMessage] = useState('');
  const [severity, setSeverity] = useState('success');

  useEffect(() => {
    if (id) {
      const fetchPessoaData = async () => {
        try {
          const response = await api.get(`/pessoa/${id}`);
          setFormData(response.data);
        } catch (error) {
          setError('Erro ao carregar os dados da pessoa.');
        }
      };
      fetchPessoaData();
    }
  }, [id]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleCepChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const cep = e.target.value.replace(/_/g, '');
    if (cep.length === 9) {
      setLoadingCep(true);
      try {
        const response = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
        const data = await response.json();
        if (data.erro) {
          setError('CEP não encontrado.');
          return;
        }
        setFormData((prevData) => ({
          ...prevData,
          cep: data.cep,
          bairro: data.bairro,
          municipio: data.localidade,
          estado: data.uf,
          complemento: data.complemento,
        }));
      } catch (error) {
        setError('Erro ao buscar o CEP.');
      } finally {
        setLoadingCep(false);
      }
    }
  };

  const handleSave = async () => {
    if (!formData.nome || !formData.cpf || !formData.telefone) {
      setError('Campos obrigatórios não preenchidos!');
      return;
    }

    try {
      if (id) {
        try {
          await api.post(`/pessoa`, formData);

          setMessage('Pessoa editada com sucesso.');
          setSeverity('success');
          setOpen(true);
        } catch (error) {
          setMessage('Erro ao processar. Tente novamente!');
          setSeverity('error');
          setOpen(true);
        }
      } else {
        try {
          await api.post('/pessoa', formData);
          setMessage('Pessoa criada com sucesso!');
          setSeverity('success');
          setOpen(true);
        } catch (error) {
          setMessage('Erro ao processar. Tente novamente!');
          setSeverity('error');
          setOpen(true);
        }
      }
      navigate('/pessoa');
    } catch (error) {
      setError('Erro ao salvar a pessoa.');
    }
  };

  return (
    <LoginBox>
      <Snackbar
        open={open}
        autoHideDuration={10000}
        onClose={handleClose}
        anchorOrigin={{
          vertical: 'top',
          horizontal: 'right',
        }}
      >
        <Alert onClose={handleClose} severity={severity} sx={{ width: '100%' }}>
          {message}
        </Alert>
      </Snackbar>
      <FormBox>
        <Typography variant="h5" gutterBottom>
          {id ? 'Editar Pessoa' : 'Adicionar Pessoa'}
        </Typography>
        {error && <Typography color="error" variant="body2" gutterBottom>{error}</Typography>}

        <TextField
          label="Nome"
          variant="outlined"
          fullWidth
          margin="normal"
          name="nome"
          value={formData.nome}
          onChange={handleChange}
        />

        <InputMask
          mask="999.999.999-99"
          value={formData.cpf}
          onChange={handleChange}
        >
          {(inputProps: any) => (
            <TextField
              {...inputProps}
              label="CPF"
              variant="outlined"
              fullWidth
              margin="normal"
              name="cpf"
            />
          )}
        </InputMask>

        <InputMask
          mask="(99) 99999-9999"
          value={formData.telefone}
          onChange={handleChange}
        >
          {(inputProps: any) => (
            <TextField
              {...inputProps}
              label="Telefone"
              variant="outlined"
              fullWidth
              margin="normal"
              name="telefone"
            />
          )}
        </InputMask>

        <TextField
          label="Número"
          variant="outlined"
          fullWidth
          margin="normal"
          name="numero"
          value={formData.numero}
          onChange={handleChange}
        />

        <TextField
          label="Complemento"
          variant="outlined"
          fullWidth
          margin="normal"
          name="complemento"
          value={formData.complemento || ''}
          onChange={handleChange}
        />

        <InputMask
          mask="99999-999"
          onChange={handleCepChange}
        >
          {(inputProps: any) => (
            <TextField
              {...inputProps}
              label="CEP"
              variant="outlined"
              fullWidth
              margin="normal"
              name="cep"
            />
          )}
        </InputMask>

        {loadingCep && <Typography variant="body2" color="textSecondary">Buscando CEP...</Typography>}

        <TextField
          label="Bairro"
          variant="outlined"
          fullWidth
          margin="normal"
          name="bairro"
          value={formData.bairro}
          onChange={handleChange}
        />
        <TextField
          label="Município"
          variant="outlined"
          fullWidth
          margin="normal"
          name="municipio"
          value={formData.municipio}
          onChange={handleChange}
          disabled
        />
        <TextField
          label="Estado"
          variant="outlined"
          fullWidth
          margin="normal"
          name="estado"
          value={formData.estado}
          onChange={handleChange}
          disabled
        />

        <Button
          variant="contained"
          fullWidth
          sx={{
            marginTop: 2,
            backgroundColor: '#ff64a3',
            '&:hover': {
              backgroundColor: '#ff4081',
            }
          }}
          onClick={handleSave}
        >
          {id ? 'Salvar Alterações' : 'Salvar'}
        </Button>
      </FormBox>
    </LoginBox>
  );
};

export default PessoaForm;
