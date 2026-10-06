/**
 * TeamForge AI - Hackathon & College Event Manager
 */

const Events = {
    eventsList: [],

    init() {
        this.bindEvents();
    },

    bindEvents() {
        const createBtn = document.getElementById('btn-create-event');
        if (createBtn) {
            createBtn.addEventListener('click', () => this.openCreateModal());
        }

        const form = document.getElementById('create-event-form');
        if (form) {
            form.addEventListener('submit', (e) => this.handleCreateEvent(e));
        }
    },

    async loadEvents() {
        try {
            const events = await API.getEvents();
            this.eventsList = events || [];
            this.renderEvents(this.eventsList);
        } catch (err) {
            App.showToast(err.message, 'error');
        }
    },

    renderEvents(events) {
        const container = document.getElementById('events-cards-grid');
        if (!container) return;

        if (!events || events.length === 0) {
            container.innerHTML = `
                <div style="text-align:center; color:var(--text-muted); padding:40px; grid-column: 1 / -1; background:var(--card-bg); border-radius:var(--radius-lg); border:1px dashed var(--card-border);">
                    <i class="fas fa-trophy" style="font-size:36px; color:var(--text-muted); margin-bottom:12px;"></i>
                    <h4>No Hackathon Events Yet</h4>
                    <p style="font-size:13px; color:var(--text-muted);">Click "Create Hackathon Event" to organize a competition and allocate student teams.</p>
                </div>
            `;
            return;
        }

        container.innerHTML = events.map(e => {
            const assigned = e.assignedTeamsCount || 0;
            const max = e.maxTeams || 8;
            const pct = Math.min(100, Math.round((assigned / max) * 100));

            return `
                <div class="event-card">
                    <div>
                        <div class="event-card-header">
                            <span class="badge badge-purple"><i class="fas fa-trophy"></i> ${e.category}</span>
                            <span class="badge ${e.status === 'UPCOMING' ? 'badge-info' : 'badge-success'}">${e.status}</span>
                        </div>
                        <div class="event-card-title">${e.title}</div>
                        <div class="event-card-desc">${e.description || 'Departmental hackathon and software exhibition event.'}</div>
                    </div>

                    <div class="event-card-footer">
                        <div style="display:flex; justify-content:space-between; align-items:center; font-size:12.5px; margin-bottom:8px;">
                            <span style="color:var(--text-muted); font-weight:600;"><i class="fas fa-calendar-alt" style="margin-right:4px;"></i> ${e.eventDate || 'TBD'}</span>
                            <span style="font-weight:700; color:var(--primary);">${assigned} / ${max} Teams Allocated</span>
                        </div>
                        <div class="progress-bar-container" style="height:8px; margin-bottom:16px;">
                            <div class="progress-fill emerald" style="width:${pct}%"></div>
                        </div>
                        <button class="btn btn-secondary btn-sm" style="width:100%; font-weight:600;" onclick="Events.openAssignTeamModal(${e.id}, '${e.title}')">
                            <i class="fas fa-user-plus"></i> Allocate Team to Event
                        </button>
                    </div>
                </div>
            `;
        }).join('');
    },

    openCreateModal() {
        document.getElementById('create-event-form').reset();
        document.getElementById('create-event-modal').classList.add('active');
    },

    closeCreateModal() {
        document.getElementById('create-event-modal').classList.remove('active');
    },

    async handleCreateEvent(e) {
        e.preventDefault();
        const payload = {
            title: document.getElementById('event-title').value,
            category: document.getElementById('event-category').value,
            description: document.getElementById('event-description').value,
            eventDate: document.getElementById('event-date').value,
            academicYear: document.getElementById('event-academic-year').value,
            maxTeams: parseInt(document.getElementById('event-max-teams').value) || 8
        };

        try {
            await API.createEvent(payload);
            App.showToast('Hackathon event created successfully!', 'success');
            this.closeCreateModal();
            this.loadEvents();
        } catch (err) {
            App.showToast(err.message, 'error');
        }
    },

    async openAssignTeamModal(eventId, eventTitle) {
        try {
            const teams = await API.getSavedTeams();
            const modal = document.getElementById('assign-team-event-modal');
            document.getElementById('assign-event-title').innerText = eventTitle;
            document.getElementById('assign-event-id').value = eventId;

            const select = document.getElementById('assign-team-select');
            if (teams && teams.length > 0) {
                select.innerHTML = teams.map(t => `<option value="${t.id}">${t.teamName} (${t.projectCategory}) - Success: ${t.successScore}%</option>`).join('');
            } else {
                select.innerHTML = `<option value="">No saved teams available</option>`;
            }

            modal.classList.add('active');
        } catch (err) {
            App.showToast(err.message, 'error');
        }
    },

    closeAssignTeamModal() {
        document.getElementById('assign-team-event-modal').classList.remove('active');
    },

    async executeTeamAllocation() {
        const eventId = document.getElementById('assign-event-id').value;
        try {
            await API.assignTeamToEvent(eventId);
            App.showToast('Team allocated to event successfully!', 'success');
            this.closeAssignTeamModal();
            this.loadEvents();
        } catch (err) {
            App.showToast(err.message, 'error');
        }
    }
};

window.Events = Events;
