/**
 * TeamForge AI - Current Teams & Team History Controller
 */
const Teams = {
    teamsList: [],

    init: async () => {
        await Teams.loadTeams();
    },

    loadTeams: async () => {
        const container = document.getElementById('saved-teams-container') || document.getElementById('teams-list-container');
        if (!container) return;

        container.innerHTML = '<div style="text-align:center; padding: 40px;"><i class="fas fa-spinner fa-spin"></i> Loading team rosters...</div>';

        try {
            const teams = await API.getSavedTeams();
            Teams.teamsList = teams || [];

            if (!teams || teams.length === 0) {
                container.innerHTML = `
                    <div style="text-align: center; padding: 40px; background: var(--card-bg); border-radius: var(--radius-lg); border: 1px dashed var(--card-border);">
                        <i class="fas fa-users" style="font-size: 36px; color: var(--text-muted); margin-bottom: 12px;"></i>
                        <h4>No Formed Teams Found</h4>
                        <p style="font-size: 13px; color: var(--text-muted);">Use the AI Team Generator to create and save optimized student teams.</p>
                    </div>
                `;
                return;
            }

            container.innerHTML = teams.map(t => {
                const members = t.members || [];
                let totalHackPart = 0, totalHackWon = 0, totalProjDone = 0;

                members.forEach(m => {
                    if (m.student) {
                        totalHackPart += (m.student.hackathonsParticipated || 0);
                        totalHackWon += (m.student.hackathonsWon || 0);
                        totalProjDone += (m.student.projectsCompleted || 0);
                    }
                });

                const teamWinRate = totalHackPart > 0 ? Math.round((totalHackWon / totalHackPart) * 1000) / 10 : 0;
                const winRateLabel = totalHackPart > 0 ? `${teamWinRate}%` : 'No History';

                return `
                    <div class="generated-team-card" style="border-top-color: var(--primary); margin-bottom: 20px;">
                        <div class="team-header-row">
                            <div class="team-title-group">
                                <h3>${t.teamName}</h3>
                                <span class="badge badge-purple">${t.projectCategory}</span>
                                <span class="badge badge-info">${t.academicYear}</span>
                            </div>
                            <div style="display: flex; align-items: center; gap: 12px;">
                                <div style="text-align: right;">
                                    <div style="font-size: 11px; color: var(--text-muted); font-weight: 700; text-transform: uppercase;">Team Success Score</div>
                                    <div style="font-size: 22px; font-weight: 800; color: var(--primary);">${t.successScore}%</div>
                                </div>
                                <span class="badge ${t.successScore >= 82 ? 'badge-success' : 'badge-warning'}">${t.predictionLabel || 'High Potential'}</span>
                            </div>
                        </div>

                        <!-- Team Historical Stats Box -->
                        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(160px, 1fr)); gap: 12px; margin-bottom: 18px; background: var(--bg-surface-alt); padding: 14px; border-radius: var(--radius-md);">
                            <div>
                                <div style="font-size: 11px; color: var(--text-muted); text-transform: uppercase; font-weight: 600;">Projects Completed</div>
                                <div style="font-size: 16px; font-weight: 800; color: var(--text-main);">${totalProjDone} Projects</div>
                            </div>
                            <div>
                                <div style="font-size: 11px; color: var(--text-muted); text-transform: uppercase; font-weight: 600;">Hackathons Participated</div>
                                <div style="font-size: 16px; font-weight: 800; color: var(--text-main);">${totalHackPart} Events</div>
                            </div>
                            <div>
                                <div style="font-size: 11px; color: var(--text-muted); text-transform: uppercase; font-weight: 600;">Hackathons Won</div>
                                <div style="font-size: 16px; font-weight: 800; color: var(--success);">${totalHackWon} Podiums</div>
                            </div>
                            <div>
                                <div style="font-size: 11px; color: var(--text-muted); text-transform: uppercase; font-weight: 600;">Cumulative Win Rate</div>
                                <div style="font-size: 16px; font-weight: 800; color: var(--primary);">${winRateLabel}</div>
                            </div>
                        </div>

                        <!-- Members list -->
                        <div class="table-responsive">
                            <table class="custom-table">
                                <thead>
                                    <tr>
                                        <th>Team Member</th>
                                        <th>Register Number</th>
                                        <th>Assigned Role</th>
                                        <th>Coding</th>
                                        <th>Design</th>
                                        <th>Communication</th>
                                        <th>Win Rate</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    ${members.map(m => `
                                        <tr>
                                            <td><b>${m.student ? m.student.name : 'Student'}</b></td>
                                            <td><span style="font-family: monospace;">${m.student ? m.student.registerNumber : 'N/A'}</span></td>
                                            <td><span class="badge badge-purple">${m.assignedRole || 'Developer'}</span></td>
                                            <td>${m.student ? m.student.codingSkill : 3}/5</td>
                                            <td>${m.student ? m.student.designSkill : 3}/5</td>
                                            <td>${m.student ? m.student.communicationSkill : 3}/5</td>
                                            <td><span class="badge badge-lime">${m.student ? m.student.winRateDisplay : 'N/A'}</span></td>
                                        </tr>
                                    `).join('')}
                                </tbody>
                            </table>
                        </div>
                    </div>
                `;
            }).join('');
        } catch (err) {
            console.error('Error loading teams list:', err);
            container.innerHTML = `<div style="color:var(--danger); padding:20px;">${err.message}</div>`;
        }
    }
};

window.Teams = Teams;
