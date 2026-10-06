/**
 * TeamForge AI - Export & Reporting Engine
 * Exports Formed Teams and Student data to CSV & Official Printable PDF/Sheet
 */

const ExportManager = {

    exportTeamsCsv(teams) {
        if (!teams || teams.length === 0) {
            App.showToast('No teams available to export.', 'warning');
            return;
        }

        let csv = 'Team Name,Category,Academic Year,Success Score (%),Potential,Member Name,Register Number,Role,Coding,Design,Communication,Documentation\n';

        teams.forEach(team => {
            if (team.members && team.members.length > 0) {
                team.members.forEach(m => {
                    csv += `"${team.teamName}","${team.projectCategory || ''}","${team.academicYear || ''}",${team.predictedSuccessScore || ''},"${team.predictionLabel || ''}","${m.name}","${m.registerNumber}","${m.assignedRole || m.preferredRole}",${m.codingSkill},${m.designSkill},${m.communicationSkill},${m.documentationSkill}\n`;
                });
            } else {
                csv += `"${team.teamName}","${team.projectCategory || ''}","${team.academicYear || ''}",${team.predictedSuccessScore || ''},"${team.predictionLabel || ''}","","","","","","",""\n`;
            }
        });

        this.downloadBlob(csv, 'teamforge_teams_roster.csv', 'text/csv;charset=utf-8;');
        App.showToast('Team roster exported successfully!', 'success');
    },

    exportStudentsCsv(students) {
        if (!students || students.length === 0) {
            App.showToast('No student data available to export.', 'warning');
            return;
        }

        let csv = 'Name,Register Number,Email,Coding (1-5),Design (1-5),Communication (1-5),Documentation (1-5),Role,Interests,Hackathons Participated,Hackathons Won,Win Rate,Projects Completed,Status\n';

        students.forEach(s => {
            csv += `"${s.name}","${s.registerNumber}","${s.email}",${s.codingSkill},${s.designSkill},${s.communicationSkill},${s.documentationSkill},"${s.preferredRole}","${(s.projectInterests || '').replace(/"/g, '""')}",${s.hackathonsParticipated},${s.hackathonsWon},"${s.winRateDisplay || ''}",${s.projectsCompleted},"${s.status}"\n`;
        });

        this.downloadBlob(csv, 'teamforge_students_directory.csv', 'text/csv;charset=utf-8;');
        App.showToast('Students directory exported successfully!', 'success');
    },

    printEvaluationReport(teams) {
        if (!teams || teams.length === 0) {
            App.showToast('No teams to generate printable evaluation report.', 'warning');
            return;
        }

        const printWindow = window.open('', '_blank', 'width=900,height=700');
        const currentDate = new Date().toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });

        let teamsHtml = '';
        teams.forEach((t, idx) => {
            let membersRows = '';
            (t.members || []).forEach(m => {
                membersRows += `
                    <tr>
                        <td style="padding: 8px; border: 1px solid #ddd;"><strong>${m.name}</strong></td>
                        <td style="padding: 8px; border: 1px solid #ddd;">${m.registerNumber}</td>
                        <td style="padding: 8px; border: 1px solid #ddd;"><span style="background: #f0fdf4; color: #166534; padding: 2px 8px; border-radius: 4px; font-weight: 600; font-size: 12px;">${m.assignedRole || m.preferredRole}</span></td>
                        <td style="padding: 8px; border: 1px solid #ddd; text-align: center;">${m.codingSkill}/5</td>
                        <td style="padding: 8px; border: 1px solid #ddd; text-align: center;">${m.designSkill}/5</td>
                        <td style="padding: 8px; border: 1px solid #ddd; text-align: center;">${m.communicationSkill}/5</td>
                        <td style="padding: 8px; border: 1px solid #ddd; text-align: center;">${m.documentationSkill}/5</td>
                    </tr>
                `;
            });

            teamsHtml += `
                <div style="margin-bottom: 28px; page-break-inside: avoid; border: 1px solid #cbd5e1; border-radius: 8px; padding: 16px; background: #fafafa;">
                    <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #059669; padding-bottom: 8px; margin-bottom: 12px;">
                        <h3 style="margin: 0; color: #0f172a; font-size: 16px;">#${idx + 1}. ${t.teamName} <span style="font-size: 13px; font-weight: normal; color: #64748b;">(${t.projectCategory || 'General CSE'})</span></h3>
                        <div style="text-align: right;">
                            <span style="font-size: 14px; font-weight: bold; color: #059669;">Success Score: ${t.predictedSuccessScore}%</span>
                            <span style="display: block; font-size: 11px; color: #64748b;">Rating: ${t.predictionLabel}</span>
                        </div>
                    </div>
                    <table style="width: 100%; border-collapse: collapse; font-size: 13px; margin-bottom: 10px; background: #fff;">
                        <thead>
                            <tr style="background: #f1f5f9; text-align: left;">
                                <th style="padding: 8px; border: 1px solid #ddd;">Student Name</th>
                                <th style="padding: 8px; border: 1px solid #ddd;">Reg No</th>
                                <th style="padding: 8px; border: 1px solid #ddd;">Assigned Role</th>
                                <th style="padding: 8px; border: 1px solid #ddd; text-align: center;">Coding</th>
                                <th style="padding: 8px; border: 1px solid #ddd; text-align: center;">Design</th>
                                <th style="padding: 8px; border: 1px solid #ddd; text-align: center;">Comm</th>
                                <th style="padding: 8px; border: 1px solid #ddd; text-align: center;">Doc</th>
                            </tr>
                        </thead>
                        <tbody>${membersRows}</tbody>
                    </table>
                    <div style="font-size: 12px; color: #334155;">
                        <strong>Key Strengths:</strong> ${(t.strengths || []).join(' • ') || 'Well balanced team.'}
                    </div>
                </div>
            `;
        });

        const docHtml = `
            <!DOCTYPE html>
            <html>
            <head>
                <title>TeamForge AI - Official Team Formation Report</title>
                <style>
                    body { font-family: 'Helvetica Neue', Arial, sans-serif; padding: 30px; color: #1e293b; }
                    @media print { body { padding: 0; } button { display: none; } }
                </style>
            </head>
            <body>
                <div style="text-align: center; border-bottom: 2px solid #0f172a; padding-bottom: 12px; margin-bottom: 24px;">
                    <h1 style="margin: 0; font-size: 22px; color: #047857; text-transform: uppercase;">TeamForge AI</h1>
                    <h2 style="margin: 4px 0; font-size: 15px; color: #1e293b;">Department of Computer Science & Engineering</h2>
                    <p style="margin: 0; font-size: 12px; color: #64748b;">Intelligent Team Allocation & Performance Prediction Report • Date: ${currentDate}</p>
                </div>
                
                <div style="margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center;">
                    <div><strong>Academic Year:</strong> 2025-2026 | <strong>Total Teams:</strong> ${teams.length}</div>
                    <button onclick="window.print()" style="background: #059669; color: white; border: none; padding: 8px 16px; border-radius: 6px; font-weight: bold; cursor: pointer;">🖨️ Print / Save as PDF</button>
                </div>

                ${teamsHtml}

                <div style="margin-top: 40px; display: flex; justify-content: space-between; page-break-inside: avoid;">
                    <div style="text-align: center; width: 220px; border-top: 1px solid #94a3b8; padding-top: 8px; font-size: 12px;">
                        <strong>Faculty Mentor Signature</strong>
                    </div>
                    <div style="text-align: center; width: 220px; border-top: 1px solid #94a3b8; padding-top: 8px; font-size: 12px;">
                        <strong>Head of Department (CSE)</strong>
                    </div>
                </div>
            </body>
            </html>
        `;

        printWindow.document.write(docHtml);
        printWindow.document.close();
    },

    downloadBlob(content, filename, contentType) {
        const blob = new Blob([content], { type: contentType });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
    }
};

window.ExportManager = ExportManager;
