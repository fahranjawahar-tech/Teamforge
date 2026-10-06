/**
 * TeamForge AI - Enhanced API Client
 */

const API = {
    baseUrl: '/api',

    async request(endpoint, options = {}) {
        const url = `${this.baseUrl}${endpoint}`;
        const headers = {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        };

        const token = sessionStorage.getItem('tf_token');
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        try {
            const res = await fetch(url, { ...options, headers });
            const contentType = res.headers.get('content-type');
            let data = null;
            if (contentType && contentType.includes('application/json')) {
                data = await res.json();
            } else {
                data = await res.text();
            }

            if (!res.ok) {
                const message = (data && data.message) || (typeof data === 'string' ? data : `Error ${res.status}`);
                throw new Error(message);
            }
            return data;
        } catch (err) {
            console.error(`API Error on [${options.method || 'GET'} ${endpoint}]:`, err);
            throw err;
        }
    },

    // Auth API
    login(username, password) {
        return this.request('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password })
        });
    },

    logout() {
        return this.request('/auth/logout', { method: 'POST' });
    },

    // Student API
    getStudents(params = {}) {
        const query = new URLSearchParams(params).toString();
        return this.request(`/students${query ? '?' + query : ''}`);
    },

    getStudentById(id) {
        return this.request(`/students/${id}`);
    },

    createStudent(studentData) {
        return this.request('/students', {
            method: 'POST',
            body: JSON.stringify(studentData)
        });
    },

    importStudentsCsv(csvData) {
        return this.request('/students/import-csv', {
            method: 'POST',
            body: JSON.stringify({ csvData })
        });
    },

    updateStudent(id, studentData) {
        return this.request(`/students/${id}`, {
            method: 'PUT',
            body: JSON.stringify(studentData)
        });
    },

    deleteStudent(id) {
        return this.request(`/students/${id}`, { method: 'DELETE' });
    },

    // AI Team Generator & Live Swapper API
    generateTeams(payload) {
        return this.request('/teams/generate', {
            method: 'POST',
            body: JSON.stringify(payload)
        });
    },

    evaluateRoster(teamName, studentIds, projectCategory, academicYear) {
        return this.request('/teams/evaluate-roster', {
            method: 'POST',
            body: JSON.stringify({ teamName, studentIds, projectCategory, academicYear })
        });
    },

    saveTeam(teamData) {
        return this.request('/teams/save', {
            method: 'POST',
            body: JSON.stringify(teamData)
        });
    },

    getSavedTeams() {
        return this.request('/teams');
    },

    // Analytics & Benchmarking API
    getDashboardStats() {
        return this.request('/analytics/dashboard');
    },

    compareTeams(teamIds) {
        return this.request('/analytics/compare', {
            method: 'POST',
            body: JSON.stringify(teamIds)
        });
    },

    getRankings(category, year) {
        const params = new URLSearchParams();
        if (category) params.append('category', category);
        if (year) params.append('year', year);
        return this.request(`/analytics/rankings${params.toString() ? '?' + params.toString() : ''}`);
    },

    // History API
    getStudentHackathons(studentId) {
        return this.request(`/history/hackathons/student/${studentId}`);
    },

    getStudentProjects(studentId) {
        return this.request(`/history/projects/student/${studentId}`);
    },

    getAllCollaborations() {
        return this.request('/history/collaborations');
    },

    // Events & Hackathons Tracker API
    getEvents() {
        return this.request('/events');
    },

    createEvent(eventData) {
        return this.request('/events', {
            method: 'POST',
            body: JSON.stringify(eventData)
        });
    },

    assignTeamToEvent(eventId) {
        return this.request(`/events/${eventId}/assign-team`, { method: 'POST' });
    }
};

window.API = API;
