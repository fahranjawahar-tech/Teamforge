/**
 * TeamForge AI - Authentication & Session Management
 */
const Auth = {
    init() {
        const loginForm = document.getElementById('login-form');
        if (loginForm) {
            loginForm.addEventListener('submit', (e) => this.handleLoginSubmit(e));
        }

        const quickLoginBtn = document.getElementById('btn-quick-faculty-login');
        if (quickLoginBtn) {
            quickLoginBtn.addEventListener('click', (e) => {
                e.preventDefault();
                this.quickLoginAsFaculty();
            });
        }
    },

    getCurrentUser() {
        const userStr = sessionStorage.getItem('tf_user') || localStorage.getItem('tf_user');
        try {
            return userStr ? JSON.parse(userStr) : null;
        } catch (e) {
            return null;
        }
    },

    getUser() {
        return this.getCurrentUser();
    },

    isAuthenticated() {
        return !!(sessionStorage.getItem('tf_token') || sessionStorage.getItem('tf_user') || localStorage.getItem('tf_token'));
    },

    setUserSession(loginResponse) {
        const token = loginResponse.token || 'demo-token-' + Date.now();
        const userObj = {
            username: loginResponse.username,
            fullName: loginResponse.fullName,
            role: loginResponse.role,
            department: loginResponse.department
        };

        sessionStorage.setItem('tf_token', token);
        sessionStorage.setItem('tf_user', JSON.stringify(userObj));
        localStorage.setItem('tf_token', token);
        localStorage.setItem('tf_user', JSON.stringify(userObj));
    },

    async logout() {
        try {
            await API.logout();
        } catch (e) {
            console.warn('Logout API call:', e);
        }
        sessionStorage.removeItem('tf_token');
        sessionStorage.removeItem('tf_user');
        localStorage.removeItem('tf_token');
        localStorage.removeItem('tf_user');

        App.showLogin();
        App.showToast('You have been logged out successfully.', 'info');
    },

    async handleLoginSubmit(e) {
        if (e && typeof e.preventDefault === 'function') {
            e.preventDefault();
        }

        const usernameInput = document.getElementById('login-username');
        const passwordInput = document.getElementById('login-password');
        const errorEl = document.getElementById('login-error-msg');
        const loginBtn = document.getElementById('login-submit-btn') || document.querySelector('#login-form button[type="submit"]');

        const username = (usernameInput ? usernameInput.value : '').trim();
        const password = (passwordInput ? passwordInput.value : '').trim();

        if (errorEl) {
            errorEl.style.display = 'none';
            errorEl.innerText = '';
        }

        if (!username) {
            if (errorEl) {
                errorEl.innerText = 'Please enter your username.';
                errorEl.style.display = 'block';
            }
            if (usernameInput) usernameInput.focus();
            return;
        }

        if (!password) {
            if (errorEl) {
                errorEl.innerText = 'Please enter your password.';
                errorEl.style.display = 'block';
            }
            if (passwordInput) passwordInput.focus();
            return;
        }

        if (loginBtn) {
            loginBtn.disabled = true;
            loginBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Authenticating...';
        }

        try {
            const res = await API.login(username, password);
            if (res && res.success) {
                this.setUserSession(res);
                App.showApp();
                App.showToast(`Welcome back, ${res.fullName || res.username}!`, 'success');
            } else {
                if (errorEl) {
                    errorEl.innerText = (res && res.message) ? res.message : 'Invalid username or password.';
                    errorEl.style.display = 'block';
                }
            }
        } catch (err) {
            console.error('Login error:', err);
            if (errorEl) {
                errorEl.innerText = err.message || 'Unable to connect to server. Please try again.';
                errorEl.style.display = 'block';
            }
        } finally {
            if (loginBtn) {
                loginBtn.disabled = false;
                loginBtn.innerHTML = '<i class="fas fa-sign-in-alt"></i> Sign In to Portal';
            }
        }
    },

    quickLoginAsFaculty() {
        const uInput = document.getElementById('login-username');
        const pInput = document.getElementById('login-password');
        if (uInput) uInput.value = 'faculty';
        if (pInput) pInput.value = 'faculty123';
        this.handleLoginSubmit();
    }
};

window.Auth = Auth;
