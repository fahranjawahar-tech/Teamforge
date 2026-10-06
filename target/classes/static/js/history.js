/**
 * TeamForge AI - Collaboration & Student History Controller
 */
const History = {
    init: async () => {
        await History.loadCollaborations();
    },

    loadCollaborations: async () => {
        const tbody = document.getElementById('collabs-table-body') || document.getElementById('collab-table-body');
        if (!tbody) return;

        tbody.innerHTML = '<tr><td colspan="6" style="text-align:center; padding:20px;"><i class="fas fa-spinner fa-spin"></i> Loading collaboration synergy records...</td></tr>';

        try {
            const list = await API.getAllCollaborations();
            if (!list || list.length === 0) {
                tbody.innerHTML = '<tr><td colspan="6" style="text-align:center; padding:20px; color:var(--text-muted);">No historical collaborations recorded yet.</td></tr>';
                return;
            }

            tbody.innerHTML = list.map(c => {
                const scoreBadge = c.collaborationScore >= 88 ? 'badge-success' : (c.collaborationScore >= 75 ? 'badge-info' : 'badge-warning');
                return `
                    <tr>
                        <td>
                            <div style="font-weight: 700; color: var(--text-main); font-size: 13.5px;">
                                <i class="fas fa-user-friends" style="color: var(--primary); margin-right: 6px;"></i>
                                ${c.student1Name}
                            </div>
                        </td>
                        <td>
                            <div style="font-weight: 700; color: var(--text-main); font-size: 13.5px;">
                                ${c.student2Name}
                            </div>
                        </td>
                        <td>
                            <span style="font-weight: 600;">${c.projectsTogether}</span> Projects Together
                        </td>
                        <td>
                            <span class="badge badge-success">${c.completedProjects} Completed</span>
                        </td>
                        <td>
                            <span style="color: var(--text-muted); font-size: 12.5px;">${c.lastProjectName || 'Collaborative Sprint'}</span>
                        </td>
                        <td>
                            <span class="badge ${scoreBadge}" style="font-size: 12px; padding: 4px 10px;">
                                ${c.collaborationScore}% Synergy
                            </span>
                        </td>
                    </tr>
                `;
            }).join('');
        } catch (err) {
            console.error('Error loading collaborations:', err);
            tbody.innerHTML = `<tr><td colspan="6" style="color:var(--danger); text-align:center; padding:20px;">${err.message}</td></tr>`;
        }
    }
};

window.History = History;
