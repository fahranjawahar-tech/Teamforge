/**
 * TeamForge AI - Team Comparison Module (Original Multi-Select Form Layout)
 */

const Comparison = {
    radarChart: null,
    teamsList: [],

    init() {
        this.bindEvents();
    },

    bindEvents() {
        const compareBtn = document.getElementById('btn-compare-teams');
        if (compareBtn) {
            compareBtn.addEventListener('click', () => this.runComparison());
        }
    },

    async loadTeamOptions() {
        try {
            const teams = await API.getSavedTeams();
            this.teamsList = teams || [];
            const select = document.getElementById('comparison-team-select');
            if (select) {
                if (teams.length === 0) {
                    select.innerHTML = '<option value="" disabled>No saved teams yet. Form teams in the Generator first!</option>';
                } else {
                    select.innerHTML = teams.map((t, idx) => `
                        <option value="${t.id}" ${idx < 2 ? 'selected' : ''}>
                            ${t.teamName} (${t.projectCategory}) - Success: ${t.successScore}%
                        </option>
                    `).join('');
                }
            }
        } catch (err) {
            App.showToast(err.message, 'error');
        }
    },

    async runComparison() {
        const select = document.getElementById('comparison-team-select');
        if (!select) return;
        
        const selectedOptions = Array.from(select.selectedOptions).map(opt => parseInt(opt.value)).filter(id => !isNaN(id));

        if (selectedOptions.length < 2) {
            App.showToast('Please hold Ctrl (or Cmd) and select at least 2 teams to compare.', 'warning');
            return;
        }

        try {
            const result = await API.compareTeams(selectedOptions);
            this.renderComparison(result);
            App.showToast('Comparative benchmarking analysis updated!', 'success');
        } catch (err) {
            App.showToast(err.message, 'error');
        }
    },

    renderComparison(result) {
        const resultsContainer = document.getElementById('comparison-results-container');
        if (resultsContainer) resultsContainer.style.display = 'block';

        // 1. Render AI Text Synthesis
        const aiTextEl = document.getElementById('comparison-ai-text');
        if (aiTextEl) {
            aiTextEl.innerHTML = `
                <div style="font-weight:700; margin-bottom:8px; color:var(--primary); font-size:14px;">
                    <i class="fas fa-microchip" style="margin-right:6px;"></i> AI Synthesis & Trade-Off Evaluation:
                </div>
                <div style="font-size:13.5px; line-height:1.6; color:var(--text-main);">
                    ${result.aiRecommendationSummary}
                </div>
            `;
        }

        // 2. Render Side-by-Side Matrix Table
        const thead = document.querySelector('#comparison-matrix-table thead');
        const tbody = document.querySelector('#comparison-matrix-table tbody');

        if (thead && tbody) {
            let headerHtml = `<tr><th style="min-width: 180px;">Metric / Dimension</th>`;
            result.teams.forEach(t => {
                headerHtml += `<th style="text-align:center; min-width: 140px;">${t.teamName}</th>`;
            });
            headerHtml += `</tr>`;
            thead.innerHTML = headerHtml;

            const metrics = [
                { label: 'Category', key: 'projectCategory' },
                { label: 'Predicted Success Score', key: 'predictedSuccessScore', suffix: '%' },
                { label: 'Potential Category', key: 'predictionLabel' },
                { label: 'Skill Score', key: 'skillScore', suffix: '%' },
                { label: 'Role Diversity Score', key: 'roleDiversityScore', suffix: '%' },
                { label: 'Experience Score', key: 'experienceScore', suffix: '%' },
                { label: 'Hackathon Performance', key: 'hackathonPerformance', suffix: '%' },
                { label: 'Synergy Compatibility', key: 'compatibilityScore', suffix: '%' },
                { label: 'Avg Coding Rating', key: 'averageCoding', suffix: '/5' },
                { label: 'Avg Design Rating', key: 'averageDesign', suffix: '/5' },
                { label: 'Avg Communication', key: 'averageCommunication', suffix: '/5' },
                { label: 'Avg Documentation', key: 'averageDocumentation', suffix: '/5' }
            ];

            let bodyHtml = '';
            metrics.forEach(m => {
                bodyHtml += `<tr><td><strong>${m.label}</strong></td>`;
                result.teams.forEach(t => {
                    const val = t[m.key] !== undefined ? t[m.key] : 'N/A';
                    let highlightStyle = '';
                    if (m.key === 'predictedSuccessScore') {
                        highlightStyle = 'color: var(--primary); font-weight: 800; font-size: 15px;';
                    }
                    bodyHtml += `<td style="text-align:center; ${highlightStyle}">${val}${m.suffix || ''}</td>`;
                });
                bodyHtml += `</tr>`;
            });
            tbody.innerHTML = bodyHtml;
        }

        // 3. Render Radar Overlay Chart with Emerald, Lime, Gold, and Deep Charcoal
        const canvas = document.getElementById('chart-comparison-radar');
        if (canvas) {
            if (this.radarChart) this.radarChart.destroy();
            const ctx = canvas.getContext('2d');

            const colorPalette = [
                { bg: 'rgba(5, 150, 105, 0.25)', border: '#059669' },
                { bg: 'rgba(132, 204, 22, 0.25)', border: '#84cc16' },
                { bg: 'rgba(234, 179, 8, 0.25)', border: '#eab308' },
                { bg: 'rgba(30, 41, 59, 0.25)', border: '#1e293b' },
                { bg: 'rgba(2, 132, 199, 0.25)', border: '#0284c7' }
            ];

            const datasets = result.teams.map((t, idx) => {
                const col = colorPalette[idx % colorPalette.length];
                return {
                    label: t.teamName,
                    data: [
                        t.skillScore || 80,
                        t.roleDiversityScore || 80,
                        t.experienceScore || 75,
                        t.hackathonPerformance || 70,
                        t.compatibilityScore || 80
                    ],
                    backgroundColor: col.bg,
                    borderColor: col.border,
                    pointBackgroundColor: col.border,
                    borderWidth: 2
                };
            });

            this.radarChart = new Chart(ctx, {
                type: 'radar',
                data: {
                    labels: ['Technical Skills', 'Role Diversity', 'Experience', 'Hackathons', 'Peer Compatibility'],
                    datasets: datasets
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        r: {
                            angleLines: { color: 'rgba(120, 113, 108, 0.15)' },
                            grid: { color: 'rgba(120, 113, 108, 0.15)' },
                            suggestedMin: 30,
                            suggestedMax: 100,
                            pointLabels: { font: { size: 12, weight: '600' }, color: '#78716c' }
                        }
                    },
                    plugins: {
                        legend: { position: 'bottom', labels: { boxWidth: 14, font: { size: 12 } } }
                    }
                }
            });
        }
    }
};

window.Comparison = Comparison;
