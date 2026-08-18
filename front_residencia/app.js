const API_URL = 'http://localhost:8080';

// Elementos DOM
const authSection = document.getElementById('auth-section');
const dashboardSection = document.getElementById('dashboard-section');
const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');
const userCrudForm = document.getElementById('user-crud-form');
const usuariosTableBody = document.getElementById('usuarios-table-body');
const btnShowRegister = document.getElementById('btn-show-register');
const btnShowLogin = document.getElementById('btn-show-login');
const btnLogout = document.getElementById('btn-logout');
const btnCancelEdit = document.getElementById('btn-cancel-edit');
const btnSaveUser = document.getElementById('btn-save-user');

// Leitura do Token JWT
function getToken() {
  return localStorage.getItem('jwt_token');
}

// Decodificação segura do payload JWT
function getUserFromToken() {
  const token = getToken();
  if (!token) return null;
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload);
  } catch (e) {
    console.error('Erro ao processar token:', e);
    return null;
  }
}

// Wrapper para requisições autenticadas
async function authFetch(endpoint, options = {}) {
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {})
  };

  const token = getToken();
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const res = await fetch(`${API_URL}${endpoint}`, { ...options, headers });

  if (res.status === 401) {
    alert('Sessão expirada. Faça login novamente.');
    logout();
    throw new Error('Não autorizado');
  }

  return res;
}

// Controle de Exibição de Telas
function showDashboard() {
  if (authSection) authSection.classList.add('hidden');
  if (dashboardSection) dashboardSection.classList.remove('hidden');
  carregarUsuarios();
}

function logout() {
  localStorage.removeItem('jwt_token');
  if (dashboardSection) dashboardSection.classList.add('hidden');
  if (authSection) authSection.classList.remove('hidden');
}

// Alternância entre Login e Cadastro
if (btnShowRegister) {
  btnShowRegister.onclick = () => {
    loginForm.classList.add('hidden');
    registerForm.classList.remove('hidden');
  };
}

if (btnShowLogin) {
  btnShowLogin.onclick = () => {
    registerForm.classList.add('hidden');
    loginForm.classList.remove('hidden');
  };
}

if (btnLogout) {
  btnLogout.onclick = logout;
}

// 1. Ação de Login
loginForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  const email = document.getElementById('login-email').value;
  const senha = document.getElementById('login-senha').value;

  try {
    const res = await fetch(`${API_URL}/api/v1/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, senha })
    });

    if (!res.ok) {
      const errText = await res.text();
      throw new Error(errText || 'Credenciais inválidas.');
    }

    const data = await res.json();
    localStorage.setItem('jwt_token', data.token);
    loginForm.reset();
    showDashboard();
  } catch (err) {
    alert(err.message);
  }
});

// 2. Ação de Registro de Nova Conta
registerForm.addEventListener('submit', async (e) => {
  e.preventDefault();

  const payload = {
    nome: document.getElementById('reg-nome').value,
    email: document.getElementById('reg-email').value,
    cpf: document.getElementById('reg-cpf').value,
    telefone: document.getElementById('reg-tel').value,
    dataNascimento: document.getElementById('reg-nasc').value,
    senha: document.getElementById('reg-senha').value,
    role: document.getElementById('reg-role').value
  };

  try {
    const res = await fetch(`${API_URL}/api/v1/auth/registro`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (!res.ok) {
      const errText = await res.text();
      throw new Error(errText || 'Erro ao realizar cadastro.');
    }

    alert('Cadastro realizado com sucesso! Faça login.');
    registerForm.reset();
    btnShowLogin.click();
  } catch (err) {
    alert(err.message);
  }
});

// 3. Listagem Dinâmica e Proteção de Perfil
async function carregarUsuarios() {
  const token = getToken();
  if (!token) return;

  const currentUser = getUserFromToken();

  try {
    const res = await authFetch('/usuarios');
    if (!res.ok) {
      console.error('Erro ao buscar dados:', res.status);
      return;
    }

    const data = await res.json();
    let lista = Array.isArray(data) ? data : (data.content || []);

    // Identifica o perfil do usuário logado na lista
    const loggedUserData = lista.find((u) => u.email === currentUser?.sub);
    const isAdmin = loggedUserData ? loggedUserData.role === 'ADMIN' : false;

    // Regra de interface: USER comum só visualiza a própria conta
    if (!isAdmin) {
      lista = lista.filter((u) => u.email === currentUser?.sub);
      userCrudForm.classList.add('hidden');
    } else {
      userCrudForm.classList.remove('hidden');
      btnSaveUser.textContent = 'Cadastrar Usuário';
    }

    usuariosTableBody.innerHTML = '';
    lista.forEach((u) => {
      const isOwner = currentUser && currentUser.sub === u.email;
      const podeEditar = isAdmin || isOwner;
      const podeExcluir = isAdmin;

      const roleBadge = u.role === 'ADMIN'
        ? `<span class="badge-admin">ADMIN</span>`
        : `<span class="badge-user">USER</span>`;

      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td><strong>${u.nome || '-'}</strong></td>
        <td>${u.email || '-'}</td>
        <td>${u.telefone || '-'}</td>
        <td>${roleBadge}</td>
        <td>
          <div class="actions-cell">
            ${podeEditar ? `<button type="button" class="btn-secondary" onclick="prepararEdicao('${u.id}', '${u.nome || ''}', '${u.email || ''}', '${u.cpf || ''}', '${u.telefone || ''}')">Editar</button>` : ''}
            ${podeExcluir ? `<button type="button" class="btn-danger" onclick="excluirUsuario('${u.id}')">Excluir</button>` : ''}
          </div>
        </td>
      `;
      usuariosTableBody.appendChild(tr);
    });
  } catch (err) {
    console.error('Erro ao renderizar a tabela:', err);
  }
}

// 4. Salvar Criação ou Atualização
userCrudForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  const id = document.getElementById('crud-id').value;
  const senha = document.getElementById('crud-senha').value;

  if (id) {
    // EDIÇÃO (PUT /usuarios/{id})
    const payload = {
      nome: document.getElementById('crud-nome').value,
      email: document.getElementById('crud-email').value,
      cpf: document.getElementById('crud-cpf').value,
      telefone: document.getElementById('crud-tel').value
    };

    if (senha.trim() !== '') {
      payload.senha = senha;
    }

    try {
      const res = await authFetch(`/usuarios/${id}`, {
        method: 'PUT',
        body: JSON.stringify(payload)
      });

      if (!res.ok) {
        const err = await res.text();
        throw new Error(err || 'Erro ao atualizar dados do usuário.');
      }

      alert('Dados atualizados com sucesso!');
      limparFormularioCrud();
      carregarUsuarios();
    } catch (err) {
      alert(err.message);
    }
  } else {
    // CRIAÇÃO DE NOVO USUÁRIO PELO ADMIN (POST /api/v1/auth/registro)
    const nasc = document.getElementById('crud-nasc').value;
    const role = document.getElementById('crud-role').value;

    if (!senha) {
      alert('Informe uma senha para o novo usuário.');
      return;
    }
    if (!nasc) {
      alert('Informe a data de nascimento.');
      return;
    }

    const payload = {
      nome: document.getElementById('crud-nome').value,
      email: document.getElementById('crud-email').value,
      cpf: document.getElementById('crud-cpf').value,
      telefone: document.getElementById('crud-tel').value,
      dataNascimento: nasc,
      senha: senha,
      role: role
    };

    try {
      const res = await fetch(`${API_URL}/api/v1/auth/registro`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!res.ok) {
        const err = await res.text();
        throw new Error(err || 'Erro ao cadastrar novo usuário.');
      }

      alert('Usuário cadastrado com sucesso!');
      limparFormularioCrud();
      carregarUsuarios();
    } catch (err) {
      alert(err.message);
    }
  }
});

// Preparar Formulário para Edição
window.prepararEdicao = (id, nome, email, cpf, telefone) => {
  document.getElementById('crud-id').value = id;
  document.getElementById('crud-nome').value = nome;
  document.getElementById('crud-email').value = email;
  document.getElementById('crud-cpf').value = cpf;
  document.getElementById('crud-tel').value = telefone;

  document.getElementById('crud-senha').value = '';
  document.getElementById('crud-senha').placeholder = 'Deixe em branco para manter a atual';
  document.getElementById('label-crud-senha').textContent = 'Nova Senha (opcional)';

  const groupNasc = document.getElementById('group-crud-nasc');
  const groupRole = document.getElementById('group-crud-role');
  if (groupNasc) groupNasc.classList.add('hidden');
  if (groupRole) groupRole.classList.add('hidden');

  btnSaveUser.textContent = 'Salvar Alterações';
  if (btnCancelEdit) btnCancelEdit.classList.remove('hidden');
  userCrudForm.classList.remove('hidden');
  userCrudForm.scrollIntoView({ behavior: 'smooth' });
};

if (btnCancelEdit) {
  btnCancelEdit.onclick = limparFormularioCrud;
}

// Limpeza e restauração do formulário
function limparFormularioCrud() {
  const currentUser = getUserFromToken();

  document.getElementById('crud-id').value = '';
  document.getElementById('crud-nome').value = '';
  document.getElementById('crud-email').value = '';
  document.getElementById('crud-cpf').value = '';
  document.getElementById('crud-tel').value = '';
  document.getElementById('crud-senha').value = '';
  document.getElementById('crud-senha').placeholder = 'Digite a senha';
  document.getElementById('label-crud-senha').textContent = 'Senha';

  const nascInput = document.getElementById('crud-nasc');
  if (nascInput) nascInput.value = '';

  const groupNasc = document.getElementById('group-crud-nasc');
  const groupRole = document.getElementById('group-crud-role');
  if (groupNasc) groupNasc.classList.remove('hidden');
  if (groupRole) groupRole.classList.remove('hidden');

  btnSaveUser.textContent = 'Cadastrar Usuário';
  if (btnCancelEdit) btnCancelEdit.classList.add('hidden');

  carregarUsuarios();
}

// Exclusão de Usuário (Apenas ADMIN)
window.excluirUsuario = async (id) => {
  if (!confirm('Deseja realmente remover este usuário?')) return;

  try {
    const res = await authFetch(`/usuarios/${id}`, { method: 'DELETE' });
    if (!res.ok) {
      const err = await res.text();
      throw new Error(err || 'Não foi possível excluir o usuário.');
    }

    carregarUsuarios();
  } catch (err) {
    alert(err.message);
  }
};

// Inicialização automática ao recarregar a página
if (getToken()) {
  showDashboard();
}