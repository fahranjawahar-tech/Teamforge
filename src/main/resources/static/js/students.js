/**
 * TeamForge AI - Enhanced Students Module with Bulk CSV Import
 */

const Students = {
    studentsList: [],

    init() {
        this.bindEvents();
    },

    bindEvents() {
        const searchInput = document.getElementById('student-search-input');
        if (searchInput) {
            searchInput.addEventListener('input', () => this.filterStudents());
        }

        const roleFilter = document.getElementById('student-role-filter');
        if (roleFilter) {
            roleFilter.addEventListener('change', () => this.loadStudents());
        }

        const addBtn = document.getElementById('btn-add-student');
        if (addBtn) {
            addBtn.addEventListener('click', () => this.openAddModal());
        }

        const importCsvBtn = document.getElementById('btn-import-csv-modal');
        if (importCsvBtn) {
            importCsvBtn.addEventListener('click', () => this.openImportCsvModal());
        }

        const exportStudentsBtn = document.getElementById('btn-export-students-csv');
        if (exportStudentsBtn) {
            exportStudentsBtn.addEventListener('click', () => {
                ExportManager.exportStudentsCsv(this.studentsList);
            });
        }

        const form = document.getElementById('student-form');
        if (form) {
            form.addEventListener('submit', (e) => this.handleSave(e));
        }

        // Live calculation of Win Rate in form
        const hackPart = document.getElementById('field-hackathons-participated');
        const hackWon = document.getElementById('field-hackathons-won');
        if (hackPart && hackWon) {
            const updateWinRate = () => {
                const p = parseInt(hackPart.value) || 0;
                const w = parseInt(hackWon.value) || 0;
                const winRateDisplay = document.getElementById('form-winrate-preview');
                if (winRateDisplay) {
                    if (p === 0) {
                        winRateDisplay.innerText = 'No Hackathons (0.0%)';
                    } else if (w > p) {
                        winRateDisplay.innerText = '⚠️ Won cannot exceed Participated';
                    } else {
                        winRateDisplay.innerText = `${((w / p) * 100).toFixed(1)}%`;
                    }
                }
            };
            hackPart.addEventListener('input', updateWinRate);
            hackWon.addEventListener('input', updateWinRate);
        }
    },

    async loadStudents() {
        const role = document.getElementById('student-role-filter')?.value || '';
        try {
            const students = await API.getStudents({ role });
            this.studentsList = students;
            this.renderTable(students);
        } catch (err) {
            App.showToast(err.message, 'error');
        }
    },

    filterStudents() {
        const q = (document.getElementById('student-search-input')?.value || '').toLowerCase().trim();
        const filtered = this.studentsList.filter(s => 
            s.name.toLowerCase().includes(q) ||
            s.registerNumber.toLowerCase().includes(q) ||
            (s.projectInterests && s.projectInterests.toLowerCase().includes(q)) ||
            (s.preferredRole && s.preferredRole.toLowerCase().includes(q))
        );
        this.renderTable(filtered);
    },

    renderTable(students) {
        const tbody = document.getElementById('students-table-body');
        if (!tbody) return;

        if (!students || students.length === 0) {
            tbody.innerHTML = `<tr><td colspan="8" style="text-align:center; padding:30px; color:var(--text-muted);">No students found.</td></tr>`;
            return;
        }

        tbody.innerHTML = students.map(s => `
            <tr>
                <td>
                    <div style="font-weight:700; color:var(--text-main);">${s.name}</div>
                    <span style="font-size:12px; color:var(--text-muted);">${s.email}</span>
                </td>
                <td><span style="font-family:monospace; font-weight:600;">${s.registerNumber}</span></td>
                <td><span class="badge badge-purple">${s.preferredRole}</span></td>
                <td>
                    <div style="display:flex; align-items:center; gap:6px;">
                        <span style="font-weight:700; font-size:13px; color:var(--primary);">${s.codingSkill}/5</span>
                        <div class="progress-bar-container" style="width:60px; height:6px;">
                            <div class="progress-fill emerald" style="width:${(s.codingSkill/5)*100}%"></div>
                        </div>
                    </div>
                </td>
                <td>
                    <div style="display:flex; align-items:center; gap:6px;">
                        <span style="font-weight:700; font-size:13px; color:var(--accent-lime);">${s.designSkill}/5</span>
                        <div class="progress-bar-container" style="width:60px; height:6px;">
                            <div class="progress-fill lime" style="width:${(s.designSkill/5)*100}%"></div>
                        </div>
                    </div>
                </td>
                <td><span class="badge badge-lime">${s.winRateDisplay || '0.0%'}</span></td>
                <td>
                    <span class="badge ${s.status === 'ASSIGNED' ? 'badge-success' : 'badge-warning'}">
                        ${s.status || 'AVAILABLE'}
                    </span>
                </td>
                <td>
                    <div style="display:flex; gap:6px;">
                        <button class="btn btn-secondary btn-sm" onclick="Students.openProfileModal(${s.id})" title="View Complete Profile & Histories">
                            <i class="fas fa-id-card"></i> Profile
                        </button>
                        <button class="btn btn-secondary btn-sm" onclick="Students.openEditModal(${s.id})" title="Edit Student">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-danger btn-sm" onclick="Students.deleteStudent(${s.id})" title="Delete Student">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `).join('');
    },

    openAddModal() {
        document.getElementById('student-modal-title').innerText = 'Add New Student';
        document.getElementById('student-form').reset();
        document.getElementById('student-id').value = '';
        document.getElementById('form-winrate-preview').innerText = '0.0%';
        document.getElementById('student-modal').classList.add('active');
    },

    async openEditModal(id) {
        try {
            const s = await API.getStudentById(id);
            document.getElementById('student-modal-title').innerText = `Edit Student - ${s.name}`;
            document.getElementById('student-id').value = s.id;
            document.getElementById('field-name').value = s.name;
            document.getElementById('field-regno').value = s.registerNumber;
            document.getElementById('field-email').value = s.email;
            document.getElementById('field-coding').value = s.codingSkill;
            document.getElementById('field-design').value = s.designSkill;
            document.getElementById('field-comm').value = s.communicationSkill;
            document.getElementById('field-doc').value = s.documentationSkill;
            document.getElementById('field-role').value = s.preferredRole;
            document.getElementById('field-interests').value = s.projectInterests || '';
            document.getElementById('field-hackathons-participated').value = s.hackathonsParticipated;
            document.getElementById('field-hackathons-won').value = s.hackathonsWon;
            document.getElementById('field-prev-projects').value = s.previousProjects;
            document.getElementById('field-completed-projects').value = s.projectsCompleted;
            document.getElementById('field-team-exp').value = s.teamExperience;
            document.getElementById('form-winrate-preview').innerText = s.winRateDisplay || '0.0%';

            document.getElementById('student-modal').classList.add('active');
        } catch (err) {
            App.showToast(err.message, 'error');
        }
    },

    closeModal() {
        document.getElementById('student-modal').classList.remove('active');
    },

    async handleSave(e) {
        e.preventDefault();
        const id = document.getElementById('student-id').value;
        const payload = {
            name: document.getElementById('field-name').value,
            registerNumber: document.getElementById('field-regno').value,
            email: document.getElementById('field-email').value,
            codingSkill: parseInt(document.getElementById('field-coding').value),
            designSkill: parseInt(document.getElementById('field-design').value),
            communicationSkill: parseInt(document.getElementById('field-comm').value),
            documentationSkill: parseInt(document.getElementById('field-doc').value),
            preferredRole: document.getElementById('field-role').value,
            projectInterests: document.getElementById('field-interests').value,
            hackathonsParticipated: parseInt(document.getElementById('field-hackathons-participated').value) || 0,
            hackathonsWon: parseInt(document.getElementById('field-hackathons-won').value) || 0,
            previousProjects: parseInt(document.getElementById('field-prev-projects').value) || 0,
            projectsCompleted: parseInt(document.getElementById('field-completed-projects').value) || 0,
            teamExperience: parseInt(document.getElementById('field-team-exp').value) || 3
        };

        try {
            if (id) {
                await API.updateStudent(id, payload);
                App.showToast('Student updated successfully!', 'success');
            } else {
                await API.createStudent(payload);
                App.showToast('Student added successfully!', 'success');
            }
            this.closeModal();
            this.loadStudents();
        } catch (err) {
            App.showToast(err.message, 'error');
        }
    },

    async deleteStudent(id) {
        if (!confirm('Are you sure you want to delete this student?')) return;
        try {
            await API.deleteStudent(id);
            App.showToast('Student deleted successfully', 'success');
            this.loadStudents();
        } catch (err) {
            App.showToast(err.message, 'error');
        }
    },

    // Detailed Student Profile Modal
    async openProfileModal(id) {
        try {
            const s = await API.getStudentById(id);
            const collabs = await API.request(`/history/collaborations/student/${id}`);

            document.getElementById('profile-name').innerText = s.name;
            document.getElementById('profile-regno').innerText = `${s.registerNumber} • ${s.email}`;
            document.getElementById('profile-role-badge').innerText = s.preferredRole;
            document.getElementById('profile-interests').innerText = s.projectInterests || 'General Software Engineering';

            document.getElementById('profile-coding-val').innerText = `${s.codingSkill}/5`;
            document.getElementById('profile-coding-bar').style.width = `${(s.codingSkill/5)*100}%`;

            document.getElementById('profile-design-val').innerText = `${s.designSkill}/5`;
            document.getElementById('profile-design-bar').style.width = `${(s.designSkill/5)*100}%`;

            document.getElementById('profile-comm-val').innerText = `${s.communicationSkill}/5`;
            document.getElementById('profile-comm-bar').style.width = `${(s.communicationSkill/5)*100}%`;

            document.getElementById('profile-doc-val').innerText = `${s.documentationSkill}/5`;
            document.getElementById('profile-doc-bar').style.width = `${(s.documentationSkill/5)*100}%`;

            document.getElementById('profile-winrate-stat').innerText = s.winRateDisplay || '0.0%';
            document.getElementById('profile-hack-counts').innerText = `${s.hackathonsWon} Won / ${s.hackathonsParticipated} Participated`;
            document.getElementById('profile-proj-stat').innerText = `${s.projectsCompleted} / ${s.previousProjects} Completed`;

            // Hackathons Timeline
            const hackList = document.getElementById('profile-hackathons-list');
            if (s.hackathonHistory && s.hackathonHistory.length > 0) {
                hackList.innerHTML = s.hackathonHistory.map(h => `
                    <div style="padding:10px 14px; background:var(--bg-surface-alt); border-radius:var(--radius-md); margin-bottom:8px; border-left:3px solid var(--accent-gold);">
                        <div style="display:flex; justify-content:space-between; font-weight:700; font-size:13px;">
                            <span>${h.hackathonName}</span>
                            <span class="badge badge-gold">${h.result}</span>
                        </div>
                        <div style="font-size:11px; color:var(--text-muted); margin-top:2px;">Year: ${h.year} | Team: ${h.teamName || 'N/A'}</div>
                    </div>
                `).join('');
            } else {
                hackList.innerHTML = `<p style="font-size:12px; color:var(--text-muted); font-style:italic;">No recorded hackathons.</p>`;
            }

            // Projects Timeline
            const projList = document.getElementById('profile-projects-list');
            if (s.projectHistory && s.projectHistory.length > 0) {
                projList.innerHTML = s.projectHistory.map(p => `
                    <div style="padding:10px 14px; background:var(--bg-surface-alt); border-radius:var(--radius-md); margin-bottom:8px; border-left:3px solid var(--primary);">
                        <div style="display:flex; justify-content:space-between; font-weight:700; font-size:13px;">
                            <span>${p.projectName}</span>
                            <span class="badge badge-success">${p.result}</span>
                        </div>
                        <div style="font-size:11px; color:var(--text-muted); margin-top:2px;">Stack: ${p.technology} | Peers: ${p.teamMembers || 'Individual'}</div>
                    </div>
                `).join('');
            } else {
                projList.innerHTML = `<p style="font-size:12px; color:var(--text-muted); font-style:italic;">No previous project records.</p>`;
            }

            // Peer Synergy Collaborations
            const collabList = document.getElementById('profile-collabs-list');
            if (collabs && collabs.length > 0) {
                collabList.innerHTML = collabs.map(c => {
                    const peerName = c.student1Id === s.id ? c.student2Name : c.student1Name;
                    return `
                        <div style="display:flex; justify-content:space-between; align-items:center; padding:8px 12px; background:var(--bg-surface-alt); border-radius:var(--radius-md); margin-bottom:6px;">
                            <span style="font-size:13px; font-weight:600;">${peerName}</span>
                            <span class="badge badge-lime">${c.collaborationScore}% Synergy</span>
                        </div>
                    `;
                }).join('');
            } else {
                collabList.innerHTML = `<p style="font-size:12px; color:var(--text-muted); font-style:italic;">No peer collaboration history.</p>`;
            }

            document.getElementById('student-profile-modal').classList.add('active');
        } catch (err) {
            App.showToast(err.message, 'error');
        }
    },

    closeProfileModal() {
        document.getElementById('student-profile-modal').classList.remove('active');
    },

    // Bulk CSV Import Modal Flow
    openImportCsvModal() {
        const modal = document.getElementById('import-csv-modal');
        document.getElementById('csv-file-input').value = '';
        document.getElementById('csv-preview-text').value = '';
        document.getElementById('csv-import-status').innerHTML = '';
        if (modal) modal.classList.add('active');
    },

    closeImportCsvModal() {
        const modal = document.getElementById('import-csv-modal');
        if (modal) modal.classList.remove('active');
    },

    handleCsvFileSelect(event) {
        const file = event.target.files[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onload = (e) => {
            document.getElementById('csv-preview-text').value = e.target.result;
        };
        reader.readAsText(file);
    },

    downloadSampleCsv() {
        const sample = `name,registerNumber,email,codingSkill,designSkill,communicationSkill,documentationSkill,preferredRole,projectInterests,hackathonsParticipated,hackathonsWon,previousProjects,projectsCompleted,teamExperience
"Naveen Raj","CSE2320","naveen.raj@college.edu",5,3,4,4,"Developer","Distributed Systems, Java, Kafka",3,2,4,4,4
"Swetha Murali","CSE2321","swetha.m@college.edu",3,5,5,4,"UI/UX Designer","Figma, Frontend, React",2,1,3,3,4
"Gautham Krishna","CSE2322","gautham.k@college.edu",4,3,5,4,"Team Lead","Full Stack, Spring Boot, Microservices",4,2,4,4,5
"Harini Sekar","CSE2323","harini.s@college.edu",4,3,4,5,"QA & Documentation","Testing, Technical Specs, DevOps",2,1,3,3,3`;

        ExportManager.downloadBlob(sample, 'sample_students_import.csv', 'text/csv;charset=utf-8;');
        App.showToast('Sample CSV template downloaded!', 'info');
    },

    async executeCsvImport() {
        const csvData = document.getElementById('csv-preview-text').value;
        if (!csvData || !csvData.trim()) {
            App.showToast('Please select a CSV file or paste CSV content.', 'warning');
            return;
        }

        const statusEl = document.getElementById('csv-import-status');
        statusEl.innerHTML = `<span style="color:var(--primary);"><i class="fas fa-spinner fa-spin"></i> Importing students into database...</span>`;

        try {
            const res = await API.importStudentsCsv(csvData);
            statusEl.innerHTML = `
                <div style="padding:10px; background:#ecfdf5; border:1px solid #a7f3d0; border-radius:6px; color:#047857; font-weight:600;">
                    ✓ Successfully imported ${res.importedCount} students! (Skipped: ${res.skippedCount})
                </div>
            `;
            App.showToast(`Imported ${res.importedCount} students successfully!`, 'success');
            this.loadStudents();
            setTimeout(() => this.closeImportCsvModal(), 1500);
        } catch (err) {
            statusEl.innerHTML = `<span style="color:var(--danger); font-weight:600;">❌ ${err.message}</span>`;
            App.showToast(err.message, 'error');
        }
    }
};

window.Students = Students;
