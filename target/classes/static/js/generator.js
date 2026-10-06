/**
 * TeamForge AI - Enhanced AI Team Generator & Live Swapper
 */

const Generator = {
    currentGeneratedTeams: [],
    selectedForSwap: null, // { teamIndex, memberIndex, studentId, studentName }

    init() {
        this.bindEvents();
    },

    bindEvents() {
        const genBtn = document.getElementById('btn-run-generator');
        if (genBtn) {
            genBtn.addEventListener('click', () => this.generateTeams());
        }

        const exportCsvBtn = document.getElementById('btn-export-teams-csv');
        if (exportCsvBtn) {
            exportCsvBtn.addEventListener('click', () => {
                ExportManager.exportTeamsCsv(this.currentGeneratedTeams);
            });
        }

        const printPdfBtn = document.getElementById('btn-print-teams-pdf');
        if (printPdfBtn) {
            printPdfBtn.addEventListener('click', () => {
                ExportManager.printEvaluationReport(this.currentGeneratedTeams);
            });
        }
    },

    async generateTeams() {
        const teamSize = parseInt(document.getElementById('gen-team-size').value) || 4;
        const projectCategory = document.getElementById('gen-category').value || 'AI & Machine Learning';
        const skillPriority = document.getElementById('gen-priority').value || 'BALANCED';
        const academicYear = document.getElementById('gen-year').value || '2025-2026';

        const payload = {
            teamSize,
            projectCategory,
            skillPriority,
            academicYear
        };

        const resultsContainer = document.getElementById('generator-results-container');
        resultsContainer.innerHTML = `
            <div style="text-align:center; padding: 40px;">
                <div style="display:inline-block; width: 40px; height: 40px; border: 4px solid var(--primary-light); border-top-color: var(--primary); border-radius: 50%; animation: spin 1s linear infinite;"></div>
                <p style="margin-top: 16px; color: var(--text-muted); font-weight: 600;">Running AI multi-constraint balancing & Python ML prediction...</p>
            </div>
            <style>@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }</style>
        `;

        try {
            const teams = await API.generateTeams(payload);
            this.currentGeneratedTeams = teams;
            this.renderTeams(teams);
            App.showToast(`AI successfully generated ${teams.length} balanced teams!`, 'success');
        } catch (err) {
            resultsContainer.innerHTML = `
                <div style="text-align:center; padding: 30px; color: var(--danger);">
                    <i class="fas fa-exclamation-triangle" style="font-size: 32px; margin-bottom: 12px;"></i>
                    <p style="font-weight: 600;">Failed to generate teams: ${err.message}</p>
                </div>
            `;
            App.showToast(err.message, 'error');
        }
    },

    renderTeams(teams) {
        const container = document.getElementById('generator-results-container');
        if (!teams || teams.length === 0) {
            container.innerHTML = `<p style="text-align:center; color:var(--text-muted); padding:30px;">No teams generated.</p>`;
            return;
        }

        const actionsToolbar = `
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 12px;">
                <div>
                    <h3 style="font-size: 18px; font-weight: 800; color: var(--text-main);">AI Formed Teams (${teams.length})</h3>
                    <p style="font-size: 12px; color: var(--text-muted);">Review scores, inspect ML feature impact, or swap members with live re-scoring.</p>
                </div>
                <div style="display: flex; gap: 10px;">
                    <button class="btn btn-secondary btn-sm" onclick="ExportManager.exportTeamsCsv(Generator.currentGeneratedTeams)">
                        <i class="fas fa-file-csv"></i> Export CSV
                    </button>
                    <button class="btn btn-secondary btn-sm" onclick="ExportManager.printEvaluationReport(Generator.currentGeneratedTeams)">
                        <i class="fas fa-print"></i> Print / PDF Report
                    </button>
                </div>
            </div>
        `;

        let cardsHtml = '';
        teams.forEach((team, teamIndex) => {
            let scoreBadgeClass = 'badge-success';
            if (team.predictedSuccessScore < 70) scoreBadgeClass = 'badge-warning';
            if (team.predictedSuccessScore < 55) scoreBadgeClass = 'badge-danger';

            // Members Table Rows with Swap Button
            let membersRows = '';
            (team.members || []).forEach((m, mIdx) => {
                membersRows += `
                    <tr>
                        <td>
                            <strong style="color: var(--text-main);">${m.name}</strong>
                            <span style="display:block; font-size: 11px; color: var(--text-muted);">${m.registerNumber}</span>
                        </td>
                        <td><span class="badge badge-purple">${m.assignedRole || m.preferredRole}</span></td>
                        <td>${m.codingSkill}/5</td>
                        <td>${m.designSkill}/5</td>
                        <td>${m.communicationSkill}/5</td>
                        <td>${m.documentationSkill}/5</td>
                        <td><span class="badge badge-lime">${m.winRateDisplay || '0.0%'}</span></td>
                        <td>
                            <button class="btn btn-secondary btn-sm" style="padding: 3px 8px; font-size: 11px;" 
                                onclick="Generator.openSwapModal(${teamIndex}, ${mIdx}, ${m.studentId}, '${m.name}')">
                                <i class="fas fa-exchange-alt"></i> Swap
                            </button>
                        </td>
                    </tr>
                `;
            });

            // Feature Contributions Waterfall
            let waterfallHtml = '';
            if (team.featureContributions && Object.keys(team.featureContributions).length > 0) {
                for (const [key, val] of Object.entries(team.featureContributions)) {
                    if (key === 'Base Cohort Score') {
                        waterfallHtml += `<span class="waterfall-pill base"><i class="fas fa-layer-group"></i> ${key}: ${val}%</span>`;
                    } else if (val >= 0) {
                        waterfallHtml += `<span class="waterfall-pill positive"><i class="fas fa-arrow-up"></i> ${key}: +${val}%</span>`;
                    } else {
                        waterfallHtml += `<span class="waterfall-pill negative"><i class="fas fa-arrow-down"></i> ${key}: ${val}%</span>`;
                    }
                }
            }

            // Diagnostic Recommendations
            let diagnosticsHtml = '';
            if (team.diagnosticRecommendations && team.diagnosticRecommendations.length > 0) {
                diagnosticsHtml = `
                    <div style="margin-top: 12px; padding: 12px; background: #f0f9ff; border: 1px solid #bae6fd; border-radius: var(--radius-md);">
                        <h5 style="font-size: 12px; font-weight: 700; color: #0369a1; margin-bottom: 6px;">
                            <i class="fas fa-tools"></i> Tailored Skill Gap & Tooling Recommendations:
                        </h5>
                        <ul style="padding-left: 18px; margin: 0; font-size: 12px; color: #0c4a6e;">
                            ${team.diagnosticRecommendations.map(r => `<li style="margin-bottom: 4px;">${r}</li>`).join('')}
                        </ul>
                    </div>
                `;
            }

            cardsHtml += `
                <div class="generated-team-card" id="team-card-${teamIndex}">
                    <div class="team-header-row">
                        <div class="team-title-group">
                            <h3>${team.teamName}</h3>
                            <span style="font-size: 12px; color: var(--text-muted);">${team.projectCategory || 'AI & Machine Learning'} • ${team.academicYear || '2025-2026'}</span>
                        </div>
                        <div style="text-align: right;">
                            <div style="font-size: 24px; font-weight: 800; color: var(--primary);">${team.predictedSuccessScore}%</div>
                            <span class="badge ${scoreBadgeClass}">${team.predictionLabel}</span>
                        </div>
                    </div>

                    <div class="metrics-pills-row">
                        <div class="metric-pill">Avg Coding: <span>${team.averageCoding || 3.0}/5</span></div>
                        <div class="metric-pill">Avg Design: <span>${team.averageDesign || 3.0}/5</span></div>
                        <div class="metric-pill">Avg Comm: <span>${team.averageCommunication || 3.0}/5</span></div>
                        <div class="metric-pill">Avg Doc: <span>${team.averageDocumentation || 3.0}/5</span></div>
                        <div class="metric-pill">Role Diversity: <span>${team.roleDiversityScore}%</span></div>
                        <div class="metric-pill">Synergy Compatibility: <span>${team.compatibilityScore}%</span></div>
                    </div>

                    <div class="table-responsive" style="margin-bottom: 16px;">
                        <table class="custom-table">
                            <thead>
                                <tr>
                                    <th>Student</th>
                                    <th>Assigned Role</th>
                                    <th>Coding</th>
                                    <th>Design</th>
                                    <th>Comm</th>
                                    <th>Doc</th>
                                    <th>Win Rate</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>${membersRows}</tbody>
                        </table>
                    </div>

                    <div class="ai-explain-box">
                        <h4><i class="fas fa-brain" style="color: var(--primary);"></i> Explainable AI Assessment</h4>
                        ${(team.strengths || []).map(s => `<div class="explain-item strength"><i class="fas fa-check-circle"></i> ${s}</div>`).join('')}
                        ${(team.weaknesses || []).map(w => `<div class="explain-item weakness"><i class="fas fa-info-circle"></i> ${w}</div>`).join('')}
                        
                        <div style="margin-top: 12px;">
                            <h5 style="font-size: 12px; font-weight: 700; color: var(--text-muted); margin-bottom: 4px;">
                                <i class="fas fa-chart-line"></i> ML Feature Contribution Waterfall (SHAP Breakdown):
                            </h5>
                            <div class="waterfall-container">${waterfallHtml}</div>
                        </div>

                        ${diagnosticsHtml}
                    </div>

                    <div style="display: flex; justify-content: flex-end; gap: 10px;">
                        <button class="btn btn-primary btn-sm" id="save-btn-${teamIndex}" onclick="Generator.saveTeam(${teamIndex})">
                            <i class="fas fa-save"></i> Save Team to System
                        </button>
                    </div>
                </div>
            `;
        });

        container.innerHTML = actionsToolbar + cardsHtml;
    },

    // Interactive Student Swapping Flow
    openSwapModal(teamIndex, memberIndex, studentId, studentName) {
        this.selectedForSwap = { teamIndex, memberIndex, studentId, studentName };
        
        const modal = document.getElementById('swap-member-modal');
        const titleEl = document.getElementById('swap-modal-student-name');
        const selectEl = document.getElementById('swap-target-student-select');

        titleEl.innerText = `${studentName} (${this.currentGeneratedTeams[teamIndex].teamName})`;
        selectEl.innerHTML = '<option value="">-- Choose Candidate to Swap With --</option>';

        this.currentGeneratedTeams.forEach((t, tIdx) => {
            if (tIdx !== teamIndex) {
                const optGroup = document.createElement('optgroup');
                optGroup.label = t.teamName;

                (t.members || []).forEach((m, mIdx) => {
                    const opt = document.createElement('option');
                    opt.value = `${tIdx}_${mIdx}_${m.studentId}`;
                    opt.textContent = `${m.name} (${m.assignedRole || m.preferredRole}) - Coding: ${m.codingSkill}/5`;
                    optGroup.appendChild(opt);
                });
                selectEl.appendChild(optGroup);
            }
        });

        modal.classList.add('active');
    },

    closeSwapModal() {
        const modal = document.getElementById('swap-member-modal');
        if (modal) modal.classList.remove('active');
        this.selectedForSwap = null;
    },

    async executeSwap() {
        if (!this.selectedForSwap) return;

        const selectEl = document.getElementById('swap-target-student-select');
        const targetVal = selectEl.value;
        if (!targetVal) {
            App.showToast('Please select a student to swap with.', 'warning');
            return;
        }

        const [t2IdxStr, m2IdxStr, s2IdStr] = targetVal.split('_');
        const t1Idx = this.selectedForSwap.teamIndex;
        const m1Idx = this.selectedForSwap.memberIndex;
        const t2Idx = parseInt(t2IdxStr);
        const m2Idx = parseInt(m2IdxStr);

        const team1 = this.currentGeneratedTeams[t1Idx];
        const team2 = this.currentGeneratedTeams[t2Idx];

        // Swap students in in-memory roster
        const tempMember = team1.members[m1Idx];
        team1.members[m1Idx] = team2.members[m2Idx];
        team2.members[m2Idx] = tempMember;

        this.closeSwapModal();
        App.showToast('Swapped students! Recalculating AI & ML scores...', 'info');

        try {
            // Live Re-evaluate both teams via backend ML service
            const ids1 = team1.members.map(m => m.studentId);
            const ids2 = team2.members.map(m => m.studentId);

            const [eval1, eval2] = await Promise.all([
                API.evaluateRoster(team1.teamName, ids1, team1.projectCategory, team1.academicYear),
                API.evaluateRoster(team2.teamName, ids2, team2.projectCategory, team2.academicYear)
            ]);

            this.currentGeneratedTeams[t1Idx] = eval1;
            this.currentGeneratedTeams[t2Idx] = eval2;

            this.renderTeams(this.currentGeneratedTeams);
            App.showToast('Dynamic AI re-scoring completed for both teams!', 'success');
        } catch (err) {
            console.error('Re-evaluation error:', err);
            this.renderTeams(this.currentGeneratedTeams);
            App.showToast('Updated roster (offline re-evaluation)', 'warning');
        }
    },

    async saveTeam(index) {
        const team = this.currentGeneratedTeams[index];
        const btn = document.getElementById(`save-btn-${index}`);

        if (btn) {
            btn.disabled = true;
            btn.innerHTML = `<i class="fas fa-spinner fa-spin"></i> Saving...`;
        }

        try {
            const res = await API.saveTeam(team);
            App.showToast(res.message || 'Team saved successfully!', 'success');
            if (btn) {
                btn.className = 'btn btn-secondary btn-sm';
                btn.innerHTML = `<i class="fas fa-check"></i> Saved`;
            }
            team.saved = true;
        } catch (err) {
            App.showToast(err.message, 'error');
            if (btn) {
                btn.disabled = false;
                btn.innerHTML = `<i class="fas fa-save"></i> Save Team`;
            }
        }
    }
};

window.Generator = Generator;
