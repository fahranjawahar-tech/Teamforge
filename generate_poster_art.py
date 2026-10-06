import matplotlib.pyplot as plt
import matplotlib.patches as patches
from PIL import Image
import os

# Configure A3 Landscape dimensions: 420mm x 297mm -> 16.54 x 11.69 inches
plt.rcParams['font.family'] = 'DejaVu Sans'
fig = plt.figure(figsize=(16.54, 11.69), dpi=300)
ax = fig.add_axes([0, 0, 1, 1])
ax.set_xlim(0, 100)
ax.set_ylim(0, 70)
ax.axis('off')

# 1. Background
bg = patches.Rectangle((0, 0), 100, 70, facecolor='#EBF4FA', edgecolor='none')
ax.add_patch(bg)

# 2. Header Banner (Navy Blue)
header_bg = patches.Rectangle((0, 61), 100, 9, facecolor='#003366', edgecolor='none')
ax.add_patch(header_bg)

# Logo Placeholders (Dashed Boxes)
logo_left = patches.Rectangle((1.5, 62), 7, 7, facecolor='#FFFFFF', edgecolor='#CBD5E1', linestyle='--', linewidth=1.2, alpha=0.3)
logo_right = patches.Rectangle((91.5, 62), 7, 7, facecolor='#FFFFFF', edgecolor='#CBD5E1', linestyle='--', linewidth=1.2, alpha=0.3)
ax.add_patch(logo_left)
ax.add_patch(logo_right)

# Header Typography
ax.text(50, 66.5, 'CHENNAI INSTITUTE OF TECHNOLOGY, CHENNAI (Autonomous)', 
        color='#FFFFFF', fontsize=18, fontweight='bold', ha='center', va='center')
ax.text(50, 64.2, 'Affiliated to Anna University, Chennai', 
        color='#E2E8F0', fontsize=12.5, ha='center', va='center')
ax.text(50, 62.2, 'Department of Computer Science and Engineering, BACHELOR OF ENGINEERING.', 
        color='#93C5FD', fontsize=11.5, fontweight='bold', ha='center', va='center')

# 3. Main Project Title
ax.text(50, 58.5, 'Smart Team Formation Optimiser for College Students', 
        color='#002855', fontsize=22, fontweight='bold', ha='center', va='center')

# Helper function to draw rounded cards with pill headers
def draw_card(x, y, w, h, title, icon=""):
    # Main white card
    card = patches.FancyBboxPatch((x, y), w, h, boxstyle="round,pad=0.3,rounding_size=1.2",
                                  facecolor='#FFFFFF', edgecolor='#60A5FA', linewidth=1.5)
    ax.add_patch(card)
    
    # Pill header (Navy Blue)
    pill_w = min(w * 0.65, 18.0)
    pill_x = x + (w - pill_w) / 2
    pill_y = y + h - 1.4
    pill = patches.FancyBboxPatch((pill_x, pill_y), pill_w, 2.2, 
                                  boxstyle="round,pad=0.15,rounding_size=1.0",
                                  facecolor='#003366', edgecolor='none')
    ax.add_patch(pill)
    
    header_text = f"{title}"
    ax.text(pill_x + pill_w / 2, pill_y + 1.1, header_text, 
            color='#FFFFFF', fontsize=11, fontweight='bold', ha='center', va='center')

# ==========================================
# TOP ROW: ABSTRACT, INTRODUCTION, OBJECTIVES
# ==========================================
draw_card(1.5, 43, 31, 13.5, "ABSTRACT")
abs_text = (
    "A full-stack Java Spring Boot web platform designed to eliminate\n"
    "manual student grouping bias in engineering coursework and hackathons.\n"
    "The platform combines multi-constraint snake-draft talent partitioning\n"
    "with a Scikit-Learn Random Forest ML microservice to balance coding,\n"
    "design, communication, and documentation while predicting team success."
)
ax.text(2.5, 52.8, abs_text, fontsize=9.2, color='#1E293B', va='top', ha='left', linespacing=1.45)

draw_card(34.5, 43, 31, 13.5, "INTRODUCTION")
intro_text = (
    "College project and hackathon teams are traditionally formed via\n"
    "ad-hoc friendship self-selection, resulting in severe skill imbalances,\n"
    "missing technical roles, and high failure rates in deliverables.\n"
    "This project delivers an automated, explainable Java web system that\n"
    "analyses student capabilities, tracks collaboration synergy, and\n"
    "forecasts team performance with actionable skill gap diagnostics."
)
ax.text(35.5, 52.8, intro_text, fontsize=9.2, color='#1E293B', va='top', ha='left', linespacing=1.45)

draw_card(67.5, 43, 31, 13.5, "OBJECTIVES")
obj_text = (
    "•  Analyze multi-attribute talent distribution in engineering cohorts.\n"
    "•  Implement Snake-Draft partitioning algorithm in Java Spring Boot.\n"
    "•  Integrate Random Forest ML Regressor (R²=0.873) for team scoring.\n"
    "•  Provide Explainable SHAP point attribution & What-If simulator.\n"
    "•  Deliver faculty governance tools: CSV import, radar matrix, PDF export."
)
ax.text(68.5, 52.8, obj_text, fontsize=9.2, color='#1E293B', va='top', ha='left', linespacing=1.45)

# ==========================================
# MIDDLE ROW: METHODS & MATERIALS & SYSTEM ARCHITECTURE
# ==========================================
draw_card(1.5, 23.5, 47, 18, "METHODS & MATERIALS")

# Flow boxes inside Methods
flow_steps = [
    ("System Design\n(Requirement\nAnalysis)", 3.0),
    ("Java Dev\n(JDK 21, Spring\nBoot 3.3.3)", 12.0),
    ("Data Handling\n(ArrayList, JPA,\nMySQL)", 21.0),
    ("ML Pipeline\n(Flask Random\nForest)", 30.0),
    ("Testing & Eval\n(JUnit 5, Live\nVerification)", 39.0)
]

for label, fx in flow_steps:
    fbox = patches.FancyBboxPatch((fx, 32.5), 7.5, 6.0, boxstyle="round,pad=0.2,rounding_size=0.6",
                                  facecolor='#F0FDF4', edgecolor='#059669', linewidth=1.2)
    ax.add_patch(fbox)
    ax.text(fx + 3.75, 35.5, label, fontsize=8.0, fontweight='bold', color='#064E3B', ha='center', va='center')
    
    # Arrow (except last)
    if fx < 39.0:
        ax.annotate('', xy=(fx + 8.8, 35.5), xytext=(fx + 7.7, 35.5),
                    arrowprops=dict(facecolor='#003366', edgecolor='#003366', arrowstyle='->', lw=2.0))

methods_footer = (
    "• Development Environment: JDK 21, IntelliJ IDEA / VS Code, Maven 3.9.9\n"
    "• Backend & Database: Spring Boot 3.3.3, Spring Data JPA, Hibernate, MySQL 8.0 / H2\n"
    "• Machine Learning: Python 3.13, Scikit-Learn 1.9.0 (Random Forest), Pandas, SHAP XAI\n"
    "• Frontend & Tools: HTML5/CSS3 SPA, Chart.js 4.4, Postman API Testing, Git Version Control"
)
ax.text(3.0, 30.5, methods_footer, fontsize=8.8, color='#1E293B', va='top', ha='left', linespacing=1.4)

draw_card(50.0, 23.5, 48.5, 18, "SYSTEM ARCHITECTURE")

# Architecture components
arch_boxes = [
    ("Students &\nFaculty", 51.5, 33.0, 6.5, 5.0, '#E0F2FE', '#0284C7'),
    ("Web Interface\n(HTML5/CSS3 SPA)", 60.5, 34.0, 11.0, 4.0, '#FEF3C7', '#D97706'),
    ("Business Logic\n(Spring Boot Services)", 74.0, 34.0, 11.5, 4.0, '#DCFCE7', '#16A34A'),
    ("Intelligence ML\n(Python Flask)", 88.0, 31.0, 9.5, 7.0, '#F3E8FF', '#9333EA'),
    ("Persistence Layer\n(Spring Data JPA / MySQL)", 65.5, 27.0, 16.0, 3.8, '#F1F5F9', '#475569')
]

for name, bx, by, bw, bh, fcol, ecol in arch_boxes:
    abox = patches.FancyBboxPatch((bx, by), bw, bh, boxstyle="round,pad=0.2,rounding_size=0.5",
                                  facecolor=fcol, edgecolor=ecol, linewidth=1.2)
    ax.add_patch(abox)
    ax.text(bx + bw/2, by + bh/2, name, fontsize=8.2, fontweight='bold', color='#0F172A', ha='center', va='center')

# Connecting arrows in Architecture
ax.annotate('', xy=(60.3, 36.0), xytext=(58.2, 36.0), arrowprops=dict(facecolor='#003366', arrowstyle='<->', lw=1.5))
ax.annotate('', xy=(73.8, 36.0), xytext=(71.7, 36.0), arrowprops=dict(facecolor='#003366', arrowstyle='<->', lw=1.5))
ax.annotate('', xy=(87.8, 36.0), xytext=(85.7, 36.0), arrowprops=dict(facecolor='#003366', arrowstyle='<->', lw=1.5))
ax.annotate('', xy=(73.5, 33.8), xytext=(73.5, 31.0), arrowprops=dict(facecolor='#003366', arrowstyle='<->', lw=1.5))
ax.annotate('', xy=(87.8, 29.0), xytext=(81.7, 29.0), arrowprops=dict(facecolor='#003366', arrowstyle='<->', lw=1.5))

arch_subtext = "Modular, decoupled multi-tier architecture with REST microservices for high scalability."
ax.text(74.0, 25.2, arch_subtext, fontsize=8.8, fontstyle='italic', color='#475569', ha='center', va='center')

# ==========================================
# BOTTOM ROW: RESULTS, DISCUSSION, CONCLUSION, ETC.
# ==========================================

# 1. DISCUSSION Card
draw_card(1.5, 12.0, 18.5, 10.5, "DISCUSSION")
disc_text = (
    "• 84.2% reduction in cohort skill variance.\n"
    "• 100% role coverage across all teams.\n"
    "• Explainable SHAP attributions eliminate\n"
    "  black-box obscurity for mentors.\n"
    "• Interactive What-If swapper accelerates\n"
    "  faculty roster tuning significantly."
)
ax.text(2.2, 19.8, disc_text, fontsize=8.2, color='#1E293B', va='top', ha='left', linespacing=1.35)

# 2. CONCLUSION Card
draw_card(1.5, 1.0, 18.5, 10.0, "CONCLUSION")
conc_text = (
    "• Successfully automates student team\n"
    "  formation using Java Spring Boot & ML.\n"
    "• Snake-draft partitioning significantly\n"
    "  outperforms ad-hoc self-selection.\n"
    "• Provides an equitable, data-driven\n"
    "  standard for engineering capstones."
)
ax.text(2.2, 8.5, conc_text, fontsize=8.2, color='#1E293B', va='top', ha='left', linespacing=1.35)

# 3. RESULTS Card (Center Table)
draw_card(21.0, 6.0, 27.5, 16.5, "RESULTS")

table_headers = ["Metric / Benchmark", "Result Value"]
table_rows = [
    ("Cohort Skill Variance Reduction", "84.2 %"),
    ("Role Diversity Defect Coverage", "100.0 %"),
    ("Random Forest Regressor R² Score", "0.873"),
    ("Model Root Mean Squared Error (RMSE)", "3.742 %"),
    ("Model Mean Absolute Error (MAE)", "2.910 %"),
    ("End-to-End API Response Latency", "62 ms"),
    ("Functional Test Suite Pass Rate", "100 % (10/10)")
]

# Draw clean table
ty = 18.5
for i, (metric, val) in enumerate(table_rows):
    bg_c = '#ECFDF5' if i % 2 == 0 else '#FFFFFF'
    row_box = patches.Rectangle((22.0, ty - 1.4), 25.5, 1.4, facecolor=bg_c, edgecolor='#E2E8F0', lw=0.5)
    ax.add_patch(row_box)
    ax.text(22.5, ty - 0.7, metric, fontsize=8.0, color='#0F172A', va='center')
    ax.text(46.8, ty - 0.7, val, fontsize=8.2, fontweight='bold', color='#059669', ha='right', va='center')
    ty -= 1.5

# 4. FUTURE ENHANCEMENTS Card
draw_card(21.0, 1.0, 27.5, 4.5, "FUTURE WORK")
f_text = "• Live GitHub/LeetCode scraping  • Docker/K8s Cloud Microservices\n• Cross-college hackathon federation  • Mobile companion app"
ax.text(34.75, 2.8, f_text, fontsize=8.0, color='#1E293B', ha='center', va='center', linespacing=1.3)

# 5. KEY FEATURES Card
draw_card(49.5, 11.5, 24.0, 11.0, "KEY FEATURES")
feat_text = (
    "✔  Snake-Draft Talent Balancing Heuristics\n"
    "✔  Explainable SHAP Point Contribution Waterfall\n"
    "✔  Interactive What-If Live Student Swapper\n"
    "✔  Multi-Axis Radar Comparison & Leaderboard\n"
    "✔  Bulk CSV Onboarding & Printable PDF Reports"
)
ax.text(50.5, 19.5, feat_text, fontsize=8.2, color='#047857', va='top', ha='left', linespacing=1.45)

# 6. REFERENCES Card
draw_card(49.5, 1.0, 24.0, 9.8, "REFERENCES")
ref_text = (
    "[1] R. M. Belbin, Management Teams (Routledge, 2010).\n"
    "[2] C. Walls, Spring in Action (Manning Publications, 2022).\n"
    "[3] F. Pedregosa et al., Scikit-learn (JMLR, 2011).\n"
    "[4] S. M. Lundberg & S.-I. Lee, SHAP XAI (NeurIPS, 2017).\n"
    "[5] Oracle Corp, Java Platform SE 21 Docs (2023)."
)
ax.text(50.5, 8.2, ref_text, fontsize=7.6, color='#334155', va='top', ha='left', linespacing=1.35)

# 7. TEAM / FACULTY Card
draw_card(74.5, 1.0, 24.0, 21.5, "TEAM / FACULTY")
team_text = (
    "STUDENTS (CSE DEPT):\n\n"
    "1. FAHRAN ALI J\n"
    "    Reg No: 2104251040235\n"
    "    Role: Backend Lead & Algorithms\n\n"
    "2. AJAY V\n"
    "    Reg No: 2104251040051\n"
    "    Role: ML Pipeline & Full-Stack UI\n\n"
    "---------------------------------------\n"
    "FACULTY GUIDE:\n"
    " [GUIDE NAME]\n"
    " [DESIGNATION]\n"
    " Department of Computer Science & Engg\n"
    " Chennai Institute of Technology\n\n"
    "ACADEMIC YEAR: 2026–2027"
)
ax.text(75.5, 19.8, team_text, fontsize=8.2, color='#0F172A', va='top', ha='left', linespacing=1.25)

# Save figures in high resolution
output_png = 'SMART_TEAM_FORMATION_OPTIMISER_POSTER.png'
output_pdf = 'SMART_TEAM_FORMATION_OPTIMISER_POSTER.pdf'

plt.savefig(output_png, dpi=300, bbox_inches='tight')
plt.savefig(output_pdf, dpi=300, bbox_inches='tight')
print(f"Poster successfully rendered and saved to {output_png} and {output_pdf}")
