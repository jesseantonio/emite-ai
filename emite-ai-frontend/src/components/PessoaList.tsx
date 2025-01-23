import React, { useEffect, useRef, useState } from 'react';
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import { Box, CircularProgress, Typography, AppBar, Toolbar, IconButton, Snackbar, Alert } from '@mui/material';
import api from '../services/api.tsx';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ProcessIcon from '@mui/icons-material/PlayCircleFilled';
import styled from 'styled-components';
import logo from '../assets/images/emite-ai-logo.png';
import { PessoaDto } from '../types/PessoaDto.tsx';
import { useNavigate } from 'react-router-dom';

const ImageBox = styled(Box)`
  width: 100%;
  display: flex;
  justify-content: flex-start;
  margin-right: 20px;
`;

const PessoaList: React.FC = () => {
  const [pessoas, setPessoas] = useState<PessoaDto[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [page, setPage] = useState<number>(0);
  const [pageSize, setPageSize] = useState<number>(10);
  const [totalPages, setTotalPages] = useState<number>(0);
  const navigate = useNavigate();
  const [selectedRows, setSelectedRows] = useState<any>([]);
  const fileInputRef = useRef<HTMLInputElement | null>(null);
  const [open, setOpen] = useState(false);
  const [message, setMessage] = useState('');
  const [severity, setSeverity] = useState('success');


  useEffect(() => {
    fetchPessoas();
  }, [page, pageSize]);

  const columns: GridColDef[] = [
    { field: 'nome', headerName: 'Nome', width: 180 },
    { field: 'cpf', headerName: 'CPF', width: 180 },
    { field: 'telefone', headerName: 'Telefone', width: 180 },
    { field: 'numero', headerName: 'Número', width: 60 },
    { field: 'complemento', headerName: 'Complemento', width: 120 },
    { field: 'cep', headerName: 'CEP', width: 120 },
    { field: 'bairro', headerName: 'Bairro', width: 120 },
    { field: 'municipio', headerName: 'Município', width: 120 },
    { field: 'estado', headerName: 'Estado', width: 120 },
    { field: 'createdBy', headerName: 'Criado por', width: 120 },
    {
      field: 'createdAt',
      headerName: 'Criado em',
      width: 150,
      valueGetter: (params: any) => formatDate(params),
    },
    { field: 'lastModificationBy', headerName: 'Atualizado por', width: 120 },
    {
      field: 'lastModificationAt',
      headerName: 'Atualizado em',
      width: 150,
      valueGetter: (params: any) => formatDate(params),
    },
    {
      field: 'actions',
      headerName: 'Ações',
      width: 150,
      renderCell: (params: any) => {
        const id = params.row.id;
        return (
          <>
            <IconButton
              color="primary"
              onClick={() => navigate(`/pessoa/${id}`)}
            >
              <EditIcon sx={{ color: '#ff64a3' }} />
            </IconButton>
            <IconButton
              color="primary"
              onClick={() => handleDelete(id)}
            >
              <DeleteIcon sx={{ color: '#ff64a3' }} />
            </IconButton>
          </>
        );
      },
    },
  ];

  const fetchPessoas = async () => {
    try {
      const response = await api.get('/pessoa', {
        params: {
          page: page,
          size: pageSize,
        },
      });
      setPessoas(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error('Erro ao buscar pessoas:', error);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${day}/${month}/${year} - ${hours}:${minutes}`;
  };

  const handlePageChange = (newPage: number) => {
    setPage(newPage);
  };

  const handlePageSizeChange = (newPageSize: number) => {
    setPageSize(newPageSize);
  };

  const handleSelectionChange = (newSelection: any) => {
    const selectedIDs = newSelection.selectionModel;
    setSelectedRows(selectedIDs);
  };

  const handleDelete = async (id: any) => {
    if (id) {
      try {
        await api.delete(`/pessoa/${id}`);
        setMessage('Pessoa com id ' + id + ' deletado com sucesso!');
        fetchPessoas();
        setSeverity('success');
        setOpen(true);
      } catch (error) {
        setMessage('Erro ao processar. Tente novamente!');
        setSeverity('error');
        setOpen(true);
      }
    }
  };

  const handleProcess = async () => {
    try {
      await api.post(`/process`);

      setMessage('Relatório está sendo gerado em segundo plano, consulte a pasta relatórios dentro do projeto que ele estará lá.');
      setSeverity('info');
      setOpen(true);
    } catch (error) {
      setMessage('Erro ao processar. Tente novamente!');
      setSeverity('error');
      setOpen(true);
    }
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <Box sx={{ padding: 2, backgroundColor: '#f5f5f5', height: '100vh' }}>

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
      {/* Toolbar */}
      <AppBar position="static" sx={{ backgroundColor: '#ffffff', marginBottom: 3 }}>
        <Toolbar>
          <ImageBox>
            <img src={logo} alt="Logo" style={{ width: '225px', height: 'auto' }} />
          </ImageBox>
          <Box sx={{ display: 'flex', justifyContent: 'flex-end', flexGrow: 1 }}>
            <IconButton color="primary" onClick={() => navigate('/pessoa/new')}>
              <AddIcon sx={{ color: '#ff64a3' }} />
            </IconButton>
            <IconButton color="inherit" onClick={handleProcess}>
              <ProcessIcon sx={{ color: '#ff64a3' }} />
            </IconButton>
          </Box>
        </Toolbar>
      </AppBar>

      {/* DataGrid */}
      <Box>
        {loading ? (
          <Box display="flex" justifyContent="center" alignItems="center" height="100px">
            <CircularProgress />
          </Box>
        ) : (
          <DataGrid
            rows={pessoas}
            columns={columns}
            pageSize={pageSize}
            rowCount={totalPages}
            paginationMode="server"
            onPageChange={handlePageChange}
            onPageSizeChange={handlePageSizeChange}
            page={page}
            pagination
            disableSelectionOnClick
            onSelectionModelChange={handleSelectionChange}
            checkboxSelection
            sx={{ backgroundColor: 'white', borderRadius: 1, width: '100%' }}
          />
        )}
      </Box>
    </Box>
  );
};

export default PessoaList;
