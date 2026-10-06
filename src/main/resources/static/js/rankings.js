/**
 * TeamForge AI - Team Rankings & Leaderboard Controller
 */
const Rankings = {
    init: async () => {
        await Rankings.loadRankings();
        Rankings.setupListeners();
    },

    setupListeners: () => {
        const catFilter = document.getElementById('rankings-category-filter');
        if (catFilter) catFilter.addEventListener('change', () => Rankings.loadRankings());
    },

    loadRankings: async () => {
        const cat = document.getElementById('rankings-category-filter')?.value || '';
        const tbody = document.getElementById('rankings-table-body');
        const podiumContainer = document.getElementById('rankings-podium-container') || document.getElementById('rankings-podium');
        if (!tbody) return;

        tbody.innerHTML = '<tr><td colspan="8" class="text-center py-4"><i class="fas fa-spinner fa-spin"></i> Loading leaderboard...</td></tr>';

        try {
            const teams = await API.getRankings(cat);

            if (!teams || teams.length === 0) {
                tbody.innerHTML = '<tr><td colspan="8" class="text-center py-4 text-muted">No teams found matching current filters.</td></tr>';
                if (podiumContainer) podiumContainer.innerHTML = '';
                return;
            }

            // Render Podium for Top 3
            if (podiumContainer && teams.length >= 3) {
                podiumContainer.innerHTML = `
                    <div style="display: flex; justify-content: center; align-items: flex-end; gap: 20px; margin-bottom: 30px; padding: 20px 0; width: 100%;">
                        <!-- 2nd Place -->
                        <div style="text-align: center; width: 200px;">
                            <div style="font-size: 28px; color: #94a3b8; margin-bottom: 6px;"><i class="fas fa-medal"></i></div>
                            <div style="background: var(--card-bg); border: 2px solid #cbd5e1; border-radius: var(--radius-md); padding: 14px; box-shadow: var(--shadow-sm);">
                                <span class="badge badge-info" style="margin-bottom: 6px;">2nd Place</span>
                                <h4 style="font-size: 15px; font-weight: 800; color: var(--text-main);">${teams[1].teamName}</h4>
                                <div style="font-size: 11px; color: var(--text-muted);">${teams[1].projectCategory}</div>
                                <div style="font-size: 20px; font-weight: 800; color: var(--primary); margin-top: 8px;">${teams[1].predictedSuccessScore}%</div>
                            </div>
                            <div style="height: 40px; background: #cbd5e1; border-radius: 4px 4px 0 0; margin-top: 8px;"></div>
                        </div>

                        <!-- 1st Place Gold -->
                        <div style="text-align: center; width: 220px;">
                            <div style="font-size: 36px; color: #eab308; margin-bottom: 6px;"><i class="fas fa-crown"></i></div>
                            <div style="background: var(--card-bg); border: 2px solid #eab308; border-radius: var(--radius-md); padding: 18px; box-shadow: var(--shadow-md);">
                                <span class="badge badge-gold" style="margin-bottom: 6px;">🏆 Champion</span>
                                <h4 style="font-size: 17px; font-weight: 800; color: var(--text-main);">${teams[0].teamName}</h4>
                                <div style="font-size: 12px; color: var(--text-muted);">${teams[0].projectCategory}</div>
                                <div style="font-size: 24px; font-weight: 800; color: #b45309; margin-top: 8px;">${teams[0].predictedSuccessScore}%</div>
                            </div>
                            <div style="height: 70px; background: #fde047; border-radius: 4px 4px 0 0; margin-top: 8px;"></div>
                        </div>

                        <!-- 3rd Place Bronze -->
                        <div style="text-align: center; width: 200px;">
                            <div style="font-size: 28px; color: #b45309; margin-bottom: 6px;"><i class="fas fa-medal"></i></div>
                            <div style="background: var(--card-bg); border: 2px solid #fed7aa; border-radius: var(--radius-md); padding: 14px; box-shadow: var(--shadow-sm);">
                                <span class="badge badge-warning" style="margin-bottom: 6px;">3rd Place</span>
                                <h4 style="font-size: 15px; font-weight: 800; color: var(--text-main);">${teams[2].teamName}</h4>
                                <div style="font-size: 11px; color: var(--text-muted);">${teams[2].projectCategory}</div>
                                <div style="font-size: 20px; font-weight: 800; color: var(--primary); margin-top: 8px;">${teams[2].predictedSuccessScore}%</div>
                            </div>
                            <div style="height: 25px; background: #fed7aa; border-radius: 4px 4px 0 0; margin-top: 8px;"></div>
                        </div>
                    </div>
                `;
            } else if (podiumContainer) {
                podiumContainer.innerHTML = '';
            }

            // Render Full Leaderboard Table
            tbody.innerHTML = teams.map((t, idx) => {
                const rankIcon = idx === 0 ? '🥇 #1' : (idx === 1 ? '🥈 #2' : (idx === 2 ? '🥉 #3' : `#${idx + 1}`));
                const membersStr = t.members ? t.members.map(m => m.name).join(', ') : 'Assigned Students';

                return `
                    <tr>
                        <td><b style="font-size: 14px; color: var(--primary);">${rankIcon}</b></td>
                        <td>
                            <b>${t.teamName}</b>
                            <div style="font-size: 11px; color: var(--text-muted);">${t.academicYear || '2025-2026'}</div>
                        </td>
                        <td><span class="badge badge-purple">${t.projectCategory}</span></td>
                        <td><div style="font-size: 12px; color: var(--text-main); font-weight: 500;">${membersStr}</div></td>
                        <td>${t.skillScore || 80}%</td>
                        <td>${t.roleDiversityScore || 85}%</td>
                        <td><strong style="color: var(--primary); font-size: 15px;">${t.predictedSuccessScore}%</strong></td>
                        <td><span class="badge ${t.predictedSuccessScore >= 82 ? 'badge-success' : 'badge-warning'}">${t.predictionLabel || 'High Potential'}</span></td>
                    </tr>
                `;
            }).join('');
        } catch (err) {
            console.error('Error loading leaderboard:', err);
            tbody.innerHTML = `<tr><td colspan="8" class="text-center text-danger">${err.message}</td></tr>`;
        }
    }
};

window.Rankings = Rankings;
