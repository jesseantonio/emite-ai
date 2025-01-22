import React, { useState } from 'react';
import { Button, TextField, Typography, Container, Box } from '@mui/material';
import styled from 'styled-components';
import logo from '../assets/images/emite-ai-logo.png';
import { useNavigate } from 'react-router-dom';


const LoginBox = styled(Box)`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: calc(100% + 200px);
  height: 100vh;
  background-color: #f0f2f5;
`;

const FormBox = styled(Box)`
  display: flex;
  flex-direction: column;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  width: 500px;
`;

const ImageBox = styled(Box)`
  width: 100%;
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
`;

const Login: React.FC = () => {
  const [username, setUsername] = useState<string>('');
  const [error, setError] = useState<string>('');
  const navigate = useNavigate();

  const handleUsername = () => {
    if (!username) {
      setError('Para continuar é necessário informar o usuário!');
      return;
    }
    localStorage.setItem('username', JSON.stringify(username));
    navigate('/pessoa');
  };

  return (
    <LoginBox>
      <FormBox>
        <ImageBox>
          <img src={logo} alt="Logo" style={{ width: '200px', height: 'auto' }} />
        </ImageBox>
        <Typography variant="h5" gutterBottom>
          Sistema de cadastro de Pessoa Física
        </Typography>
        {error && <Typography color="error" variant="body2" gutterBottom>{error}</Typography>}
        <TextField
          label="Usuário"
          variant="outlined"
          fullWidth
          margin="normal"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <Button
          variant="contained"
          fullWidth
          onClick={handleUsername}
          sx={{
            marginTop: 2,
            backgroundColor: '#ff64a3',  // Cor personalizada
            '&:hover': {
              backgroundColor: '#ff4081',  // Cor ao passar o mouse
            }
          }}
        >
          Entrar
        </Button>
      </FormBox>
    </LoginBox>
  );
};

export default Login;
