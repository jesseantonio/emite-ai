import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Login from './components/Login.tsx';
import PessoaList from './components/PessoaList.tsx';
import PessoaForm from './components/PessoaForm.tsx';

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route
          path="/pessoa" element={localStorage.getItem('username') ? <PessoaList /> : <Navigate to="/login" />}
        />
        <Route path="/pessoa/new" element={<PessoaForm />} />
        <Route path="/pessoa/:id" element={<PessoaForm />} />
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
};

export default App;
