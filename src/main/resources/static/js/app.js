/**
 * TeamForge AI - Main Application Router & Orchestrator
 */

const App = {
    currentView: 'dashboard',

    init() {
        this.bindNavigation();
        this.bindThemeToggle();
        
        try {
            Auth.init();
        } catch (e) {
            console.error('Auth init error:', e);
        }

        if (Auth.isAuthenticated()) {
            this.showApp();
        } else {
            this.showLogin();
        }
    },

    bindNavigation() {
        const navLinks = document.querySelectorAll('.sidebar-nav .nav-link');
        navLinks.forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const view = link.getAttribute('data-view');
                if (view) {
                    this.navigateTo(view);
                }
            });
        });

        const logoutBtn = document.getElementById('btn-logout');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', () => Auth.logout());
        }
    },

    bindThemeToggle() {
        const toggleBtn = document.getElementById('theme-toggle');
        if (toggleBtn) {
            const savedTheme = localStorage.getItem('tf_theme') || 'light';
            document.documentElement.setAttribute('data-theme', savedTheme);
            this.updateThemeIcon(savedTheme);

            toggleBtn.addEventListener('click', () => {
                const current = document.documentElement.getAttribute('data-theme') || 'light';
                const next = current === 'light' ? 'dark' : 'light';
                document.documentElement.setAttribute('data-theme', next);
                localStorage.setItem('tf_theme', next);
                this.updateThemeIcon(next);
                this.showToast(`Switched to ${next === 'dark' ? 'Dark Charcoal' : 'Warm Cream'} mode`, 'info');
            });
        }
    },

    updateThemeIcon(theme) {
        const icon = document.querySelector('#theme-toggle i');
        if (icon) {
            if (theme === 'dark') {
                icon.className = 'fas fa-sun';
            } else {
                icon.className = 'fas fa-moon';
            }
        }
    },

    showLogin() {
        const loginPage = document.getElementById('login-page');
        const appContainer = document.getElementById('app-container');
        if (loginPage) loginPage.style.display = 'flex';
        if (appContainer) appContainer.style.display = 'none';
    },

    showApp() {
        const loginPage = document.getElementById('login-page');
        const appContainer = document.getElementById('app-container');
        if (loginPage) loginPage.style.display = 'none';
        if (appContainer) appContainer.style.display = 'flex';

        const user = Auth.getCurrentUser();
        if (user) {
            const nameEl = document.getElementById('user-display-name');
            const roleEl = document.getElementById('user-display-role');
            const avatarEl = document.getElementById('user-avatar-initial');
            if (nameEl) nameEl.innerText = user.fullName || user.username;
            if (roleEl) roleEl.innerText = user.department || user.role;
            if (avatarEl) avatarEl.innerText = (user.fullName || user.username || 'F').charAt(0).toUpperCase();
        }

        try { Students.init(); } catch (e) { console.warn('Students init:', e); }
        try { Generator.init(); } catch (e) { console.warn('Generator init:', e); }
        try { Comparison.init(); } catch (e) { console.warn('Comparison init:', e); }
        try { Teams.init(); } catch (e) { console.warn('Teams init:', e); }
        try { Rankings.init(); } catch (e) { console.warn('Rankings init:', e); }
        try { History.init(); } catch (e) { console.warn('History init:', e); }
        try { Events.init(); } catch (e) { console.warn('Events init:', e); }

        this.navigateTo('dashboard');
    },

    navigateTo(viewId) {
        this.currentView = viewId;

        document.querySelectorAll('.sidebar-nav .nav-link').forEach(link => {
            if (link.getAttribute('data-view') === viewId) {
                link.classList.add('active');
            } else {
                link.classList.remove('active');
            }
        });

        document.querySelectorAll('.view-panel').forEach(panel => {
            panel.style.display = 'none';
        });

        const targetPanel = document.getElementById(`view-${viewId}`);
        if (targetPanel) {
            targetPanel.style.display = 'block';
        }

        const titleMap = {
            'dashboard': 'Faculty Analytics Dashboard',
            'students': 'Student Directory & Skills',
            'generator': 'AI Team Generator & Live Swapper',
            'events': 'Hackathon & Project Events Tracker',
            'comparison': 'Multi-Team Radar & Matrix Comparison',
            'teams': 'Formed & Historical Teams',
            'rankings': 'Team Rankings & Leaderboard',
            'collaborations': 'Pairwise Collaboration Synergy'
        };

        const titleEl = document.getElementById('current-page-title');
        if (titleEl) {
            titleEl.innerText = titleMap[viewId] || 'TeamForge AI Platform';
        }

        // Lazy view data loaders
        try {
            if (viewId === 'dashboard') Dashboard.load();
            else if (viewId === 'students') Students.loadStudents();
            else if (viewId === 'events') Events.loadEvents();
            else if (viewId === 'teams') Teams.loadTeams();
            else if (viewId === 'comparison') Comparison.loadTeamOptions();
            else if (viewId === 'rankings') Rankings.loadRankings();
            else if (viewId === 'collaborations') History.loadCollaborations();
        } catch (e) {
            console.error(`Error loading view ${viewId}:`, e);
        }
    },

    showToast(message, type = 'success') {
        const container = document.getElementById('toast-container');
        if (!container) return;

        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        
        let icon = 'fa-check-circle';
        if (type === 'error') icon = 'fa-exclamation-circle';
        if (type === 'warning') icon = 'fa-exclamation-triangle';
        if (type === 'info') icon = 'fa-info-circle';

        toast.innerHTML = `<i class="fas ${icon}"></i> <span>${message}</span>`;
        container.appendChild(toast);

        setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transform = 'translateY(10px)';
            setTimeout(() => toast.remove(), 300);
        }, 3500);
    }
};

document.addEventListener('DOMContentLoaded', () => {
    App.init();
});

window.App = App;
