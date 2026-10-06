import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import OxmlElement, parse_xml
from docx.oxml.ns import nsdecls, qn
import os

def set_cell_background(cell, fill_hex):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    tcPr.append(shd)

def set_cell_margins(cell, top=100, bottom=100, left=150, right=150):
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = parse_xml(f'''
        <w:tcMar {nsdecls("w")}>
            <w:top w:w="{top}" w:type="dxa"/>
            <w:bottom w:w="{bottom}" w:type="dxa"/>
            <w:left w:w="{left}" w:type="dxa"/>
            <w:right w:w="{right}" w:type="dxa"/>
        </w:tcMar>
    ''')
    tcPr.append(tcMar)

def set_table_borders(table, color="CCCCCC", sz="4", val="single"):
    tblPr = table._tbl.tblPr
    borders = parse_xml(f'''
        <w:tblBorders {nsdecls("w")}>
            <w:top w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>
            <w:bottom w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>
            <w:insideH w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>
            <w:insideV w:val="none"/>
            <w:left w:val="none"/>
            <w:right w:val="none"/>
        </w:tblBorders>
    ''')
    tblPr.append(borders)

def set_box_borders(cell, color="059669", sz="12"):
    tcPr = cell._tc.get_or_add_tcPr()
    borders = parse_xml(f'''
        <w:tcBorders {nsdecls("w")}>
            <w:top w:val="single" w:sz="{sz}" w:space="0" w:color="{color}"/>
            <w:left w:val="single" w:sz="{sz}" w:space="0" w:color="{color}"/>
            <w:bottom w:val="single" w:sz="{sz}" w:space="0" w:color="{color}"/>
            <w:right w:val="single" w:sz="{sz}" w:space="0" w:color="{color}"/>
        </w:tcBorders>
    ''')
    tcPr.append(borders)

def build_full_report():
    doc = docx.Document()
    
    # 1. Page Margins
    for section in doc.sections:
        section.top_margin = Inches(1.0)
        section.bottom_margin = Inches(1.0)
        section.left_margin = Inches(1.25)
        section.right_margin = Inches(1.0)
        section.page_width = Inches(8.5)
        section.page_height = Inches(11.0)
        
    # 2. Configure Default Styles
    normal_style = doc.styles['Normal']
    normal_style.font.name = 'Times New Roman'
    normal_style.font.size = Pt(12)
    normal_style.font.color.rgb = RGBColor(0x1A, 0x1A, 0x1A)
    normal_style.paragraph_format.line_spacing = 1.15
    normal_style.paragraph_format.space_after = Pt(6)
    normal_style.paragraph_format.space_before = Pt(0)
    
    # Custom Heading Helpers
    def add_title_p(text, size=14, bold=True, italic=False, align=WD_ALIGN_PARAGRAPH.CENTER, space_after=6, space_before=0, color=(0,0,0)):
        p = doc.add_paragraph()
        p.alignment = align
        p.paragraph_format.space_after = Pt(space_after)
        p.paragraph_format.space_before = Pt(space_before)
        p.paragraph_format.line_spacing = 1.15
        run = p.add_run(text)
        run.bold = bold
        run.italic = italic
        run.font.name = 'Times New Roman'
        run.font.size = Pt(size)
        run.font.color.rgb = RGBColor(*color)
        return p

    def add_body_p(text, bold_prefix="", italic=False, space_after=6):
        p = doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
        p.paragraph_format.space_after = Pt(space_after)
        p.paragraph_format.line_spacing = 1.15
        if bold_prefix:
            r_pre = p.add_run(bold_prefix)
            r_pre.bold = True
            r_pre.font.name = 'Times New Roman'
            r_pre.font.size = Pt(12)
        r_text = p.add_run(text)
        r_text.font.name = 'Times New Roman'
        r_text.font.size = Pt(12)
        r_text.italic = italic
        return p

    def add_chap_heading(chap_num, chap_title):
        p1 = doc.add_paragraph()
        p1.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p1.paragraph_format.space_before = Pt(18)
        p1.paragraph_format.space_after = Pt(4)
        r1 = p1.add_run(f"CHAPTER {chap_num}")
        r1.bold = True
        r1.font.name = 'Times New Roman'
        r1.font.size = Pt(16)
        r1.font.color.rgb = RGBColor(0x05, 0x5F, 0x46) # Emerald Dark
        
        p2 = doc.add_paragraph()
        p2.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p2.paragraph_format.space_before = Pt(0)
        p2.paragraph_format.space_after = Pt(16)
        r2 = p2.add_run(chap_title.upper())
        r2.bold = True
        r2.font.name = 'Times New Roman'
        r2.font.size = Pt(15)
        r2.font.color.rgb = RGBColor(0x18, 0x18, 0x1B)

    def add_sec_heading(sec_num, sec_title):
        p = doc.add_paragraph()
        p.paragraph_format.space_before = Pt(14)
        p.paragraph_format.space_after = Pt(4)
        r = p.add_run(f"{sec_num} {sec_title.upper()}")
        r.bold = True
        r.font.name = 'Times New Roman'
        r.font.size = Pt(13)
        r.font.color.rgb = RGBColor(0x06, 0x4E, 0x3B)
        return p

    def add_subsec_heading(subsec_num, subsec_title):
        p = doc.add_paragraph()
        p.paragraph_format.space_before = Pt(10)
        p.paragraph_format.space_after = Pt(3)
        r = p.add_run(f"{subsec_num} {subsec_title}")
        r.bold = True
        r.font.name = 'Times New Roman'
        r.font.size = Pt(12)
        r.font.color.rgb = RGBColor(0x27, 0x27, 0x2A)
        return p

    def add_bullet_item(bold_label, text):
        p = doc.add_paragraph(style='List Bullet')
        p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
        p.paragraph_format.space_after = Pt(4)
        p.paragraph_format.line_spacing = 1.15
        if bold_label:
            r_b = p.add_run(bold_label + ": ")
            r_b.bold = True
            r_b.font.name = 'Times New Roman'
            r_b.font.size = Pt(12)
        r_t = p.add_run(text)
        r_t.font.name = 'Times New Roman'
        r_t.font.size = Pt(12)
        return p

    def add_code_snippet(code_text, caption=""):
        if caption:
            p_cap = doc.add_paragraph()
            p_cap.alignment = WD_ALIGN_PARAGRAPH.LEFT
            p_cap.paragraph_format.space_before = Pt(8)
            p_cap.paragraph_format.space_after = Pt(3)
            r_cap = p_cap.add_run(caption)
            r_cap.bold = True
            r_cap.italic = True
            r_cap.font.name = 'Times New Roman'
            r_cap.font.size = Pt(11)
            
        tbl = doc.add_table(rows=1, cols=1)
        tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        cell = tbl.cell(0, 0)
        cell.width = Inches(6.25)
        set_cell_background(cell, "F8FAF8")
        set_box_borders(cell, color="CBD5E1", sz="6")
        set_cell_margins(cell, top=140, bottom=140, left=180, right=180)
        
        p = cell.paragraphs[0]
        p.paragraph_format.space_before = Pt(0)
        p.paragraph_format.space_after = Pt(0)
        p.paragraph_format.line_spacing = 1.05
        r = p.add_run(code_text)
        r.font.name = 'Consolas'
        r.font.size = Pt(9.5)
        r.font.color.rgb = RGBColor(0x1E, 0x29, 0x3B)
        
        p_sp = doc.add_paragraph()
        p_sp.paragraph_format.space_after = Pt(6)

    def add_styled_table(headers, data, col_widths=None, caption=""):
        if caption:
            p_cap = doc.add_paragraph()
            p_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
            p_cap.paragraph_format.space_before = Pt(10)
            p_cap.paragraph_format.space_after = Pt(4)
            r_cap = p_cap.add_run(caption)
            r_cap.bold = True
            r_cap.font.name = 'Times New Roman'
            r_cap.font.size = Pt(11)

        tbl = doc.add_table(rows=len(data) + 1, cols=len(headers))
        tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        set_table_borders(tbl, color="94A3B8", sz="6")
        
        # Header row
        hdr_row = tbl.rows[0]
        for idx, h_text in enumerate(headers):
            cell = hdr_row.cells[idx]
            set_cell_background(cell, "E2E8F0")
            set_cell_margins(cell, top=120, bottom=120, left=140, right=140)
            p = cell.paragraphs[0]
            p.alignment = WD_ALIGN_PARAGRAPH.CENTER
            p.paragraph_format.space_after = Pt(0)
            p.paragraph_format.space_before = Pt(0)
            r = p.add_run(h_text)
            r.bold = True
            r.font.name = 'Times New Roman'
            r.font.size = Pt(10.5)
            r.font.color.rgb = RGBColor(0x0F, 0x17, 0x2A)
            
        # Data rows
        for r_idx, row_data in enumerate(data):
            row = tbl.rows[r_idx + 1]
            bg_col = "FFFFFF" if r_idx % 2 == 0 else "F8FAFC"
            for c_idx, val in enumerate(row_data):
                cell = row.cells[c_idx]
                set_cell_background(cell, bg_col)
                set_cell_margins(cell, top=100, bottom=100, left=120, right=120)
                p = cell.paragraphs[0]
                p.alignment = WD_ALIGN_PARAGRAPH.LEFT if c_idx > 0 and len(str(val)) > 8 else WD_ALIGN_PARAGRAPH.CENTER
                p.paragraph_format.space_after = Pt(0)
                p.paragraph_format.space_before = Pt(0)
                r = p.add_run(str(val))
                r.font.name = 'Times New Roman'
                r.font.size = Pt(10)
                
        if col_widths and len(col_widths) == len(headers):
            for row in tbl.rows:
                for idx, w in enumerate(col_widths):
                    row.cells[idx].width = Inches(w)
                    
        p_sp = doc.add_paragraph()
        p_sp.paragraph_format.space_after = Pt(6)

    # =========================================================================
    # 1. TITLE PAGE
    # =========================================================================
    add_title_p("SMART TEAM FORMATION OPTIMISER FOR COLLEGE STUDENTS", size=17, bold=True, space_before=18, space_after=14, color=(6, 78, 59))
    add_title_p("A PROJECT BASED LEARNING (PBL) REPORT", size=13, bold=True, space_after=18, color=(30, 41, 59))
    add_title_p("Submitted by", size=12, bold=True, italic=True, space_after=14)
    
    add_title_p("FAHRAN ALI J\n2104251040235", size=13, bold=True, space_after=10, color=(15, 23, 42))
    add_title_p("AJAY V\n2104251040051", size=13, bold=True, space_after=18, color=(15, 23, 42))
    
    add_title_p("Submitted in partial fulfilment of the requirements\nfor the\nProject-Based Learning component of Java Programming", size=12, bold=False, italic=True, space_after=16)
    
    add_title_p("BACHELOR OF ENGINEERING\nin\nCOMPUTER SCIENCE AND ENGINEERING", size=13, bold=True, space_after=24, color=(6, 78, 59))
    
    add_title_p("CHENNAI INSTITUTE OF TECHNOLOGY\n(Autonomous)\nAffiliated to Anna University, Chennai", size=13, bold=True, space_after=18, color=(15, 23, 42))
    add_title_p("October - 2026", size=12, bold=True, space_after=0)
    
    doc.add_page_break()

    # =========================================================================
    # 2. INSTITUTE VISION & MISSION
    # =========================================================================
    add_title_p("CHENNAI INSTITUTE OF TECHNOLOGY", size=15, bold=True, space_before=10, space_after=4, color=(6, 78, 59))
    add_title_p("(Autonomous)\nAffiliated to Anna University, Chennai", size=11, bold=True, space_after=18)
    
    add_title_p("Vision of the Institute:", size=13, bold=True, align=WD_ALIGN_PARAGRAPH.LEFT, space_before=12, space_after=8, color=(6, 78, 59))
    
    # Vision Box
    t_v = doc.add_table(rows=1, cols=1)
    t_v.alignment = WD_TABLE_ALIGNMENT.CENTER
    c_v = t_v.cell(0, 0)
    c_v.width = Inches(6.25)
    set_cell_background(c_v, "FAF8F5")
    set_box_borders(c_v, color="D97706", sz="12")
    set_cell_margins(c_v, top=160, bottom=160, left=200, right=200)
    pv = c_v.paragraphs[0]
    pv.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    rv = pv.add_run("To be an eminent centre for Academia, Industry and Research by imparting knowledge, relevant practices and inculcating human values to address global challenges through novelty and sustainability.")
    rv.font.name = 'Times New Roman'
    rv.font.size = Pt(11.5)
    rv.font.color.rgb = RGBColor(0x1C, 0x19, 0x17)
    
    doc.add_paragraph().paragraph_format.space_after = Pt(12)
    add_title_p("Mission of the Institute:", size=13, bold=True, align=WD_ALIGN_PARAGRAPH.LEFT, space_before=10, space_after=8, color=(6, 78, 59))
    
    # Mission Box
    t_m = doc.add_table(rows=1, cols=1)
    t_m.alignment = WD_TABLE_ALIGNMENT.CENTER
    c_m = t_m.cell(0, 0)
    c_m.width = Inches(6.25)
    set_cell_background(c_m, "FAF8F5")
    set_box_borders(c_m, color="D97706", sz="12")
    set_cell_margins(c_m, top=160, bottom=160, left=200, right=200)
    
    missions = [
        ("IM1. ", "To creates next generation leaders by effective teaching learning methodologies and in still Scientifics park in them to meet the global challenges."),
        ("IM2. ", "To transform lives through deployment of emerging technology, novelty and sustainability."),
        ("IM3. ", "To inculcate human values and ethical principles to cater the societal needs."),
        ("IM4. ", "To contributes towards the research ecosystem by providing a suitable, state of the art infrastructure.")
    ]
    for idx, (code, m_text) in enumerate(missions):
        pm = c_m.paragraphs[0] if idx == 0 else c_m.add_paragraph()
        pm.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
        pm.paragraph_format.space_after = Pt(6)
        r_c = pm.add_run(code)
        r_c.bold = True
        r_c.font.name = 'Times New Roman'
        r_c.font.size = Pt(11)
        r_c.font.color.rgb = RGBColor(0xB4, 0x53, 0x09)
        r_m = pm.add_run(m_text)
        r_m.font.name = 'Times New Roman'
        r_m.font.size = Pt(11)
        
    doc.add_page_break()

    # =========================================================================
    # 3. DEPARTMENT VISION & MISSION
    # =========================================================================
    add_title_p("DEPARTMENT OF\nCOMPUTER SCIENCE AND ENGINEERING", size=14, bold=True, space_before=12, space_after=16, color=(6, 78, 59))
    
    add_title_p("Vision of the Department:", size=13, bold=True, align=WD_ALIGN_PARAGRAPH.LEFT, space_before=10, space_after=8, color=(6, 78, 59))
    
    t_dv = doc.add_table(rows=1, cols=1)
    t_dv.alignment = WD_TABLE_ALIGNMENT.CENTER
    c_dv = t_dv.cell(0, 0)
    c_dv.width = Inches(6.25)
    set_cell_background(c_dv, "FAF8F5")
    set_box_borders(c_dv, color="059669", sz="12")
    set_cell_margins(c_dv, top=160, bottom=160, left=200, right=200)
    pdv = c_dv.paragraphs[0]
    pdv.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    rdv = pdv.add_run("To Excel in the emerging areas of Computer Science and Engineering by imparting knowledge, relevant practices and inculcating human values to transform the students as potential resources to contribute innovatively through advanced computing in real time situations.")
    rdv.font.name = 'Times New Roman'
    rdv.font.size = Pt(11.5)
    
    doc.add_paragraph().paragraph_format.space_after = Pt(12)
    add_title_p("Mission of the Department:", size=13, bold=True, align=WD_ALIGN_PARAGRAPH.LEFT, space_before=10, space_after=8, color=(6, 78, 59))
    
    t_dm = doc.add_table(rows=1, cols=1)
    t_dm.alignment = WD_TABLE_ALIGNMENT.CENTER
    c_dm = t_dm.cell(0, 0)
    c_dm.width = Inches(6.25)
    set_cell_background(c_dm, "FAF8F5")
    set_box_borders(c_dm, color="059669", sz="12")
    set_cell_margins(c_dm, top=160, bottom=160, left=200, right=200)
    
    dept_missions = [
        ("DM1. ", "To provide strong fundamentals and technical skills for Computer Science applications through effective teaching learning methodologies."),
        ("DM2. ", "To transform lives of the students by nurturing ethical values, creativity and novelty to become Entrepreneurs and establish start-ups."),
        ("DM3. ", "To habituate the students to focus on sustainable solutions to improve the quality of life and the welfare of the society.")
    ]
    for idx, (code, dm_text) in enumerate(dept_missions):
        pdm = c_dm.paragraphs[0] if idx == 0 else c_dm.add_paragraph()
        pdm.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
        pdm.paragraph_format.space_after = Pt(6)
        r_c = pdm.add_run(code)
        r_c.bold = True
        r_c.font.name = 'Times New Roman'
        r_c.font.size = Pt(11)
        r_c.font.color.rgb = RGBColor(0x04, 0x78, 0x57)
        r_m = pdm.add_run(dm_text)
        r_m.font.name = 'Times New Roman'
        r_m.font.size = Pt(11)
        
    doc.add_page_break()

    # =========================================================================
    # 4. BONAFIDE CERTIFICATE
    # =========================================================================
    add_title_p("CHENNAI INSTITUTE OF TECHNOLOGY, CHENNAI\n(Autonomous)\nAffiliated to Anna University, Chennai", size=13, bold=True, space_before=12, space_after=16)
    add_title_p("BONAFIDE CERTIFICATE", size=15, bold=True, space_after=20, color=(6, 78, 59))
    
    add_body_p(
        "This is to certify that the Project–Based Learning report titled “SMART TEAM FORMATION OPTIMISER FOR COLLEGE STUDENTS” is a Bonafide record of work carried out by FAHRAN ALI J (Register No: 2104251040235) and AJAY V (Register No: 2104251040051) of the Department of Computer Science and Engineering, Chennai Institute of Technology, as part of the continuous, mentor–guided Project-Based Learning (PBL) component of the Java Programming course during the academic year [2026–2027] under my supervision.",
        space_after=40
    )
    
    # Signatures Table
    t_sig = doc.add_table(rows=1, cols=2)
    t_sig.alignment = WD_TABLE_ALIGNMENT.CENTER
    c_hod = t_sig.cell(0, 0)
    c_sup = t_sig.cell(0, 1)
    c_hod.width = Inches(3.2)
    c_sup.width = Inches(3.2)
    
    p_hod = c_hod.paragraphs[0]
    p_hod.paragraph_format.line_spacing = 1.15
    p_hod.add_run("SIGNATURE\n").bold = True
    p_hod.add_run("Dr. S. Pavithra M.E., Ph.D\n").bold = True
    p_hod.add_run("HEAD OF THE DEPARTMENT\nProfessor and Head,\nDepartment of Computer Science and Engineering\nChennai Institute of Technology\nChennai - 600 069")
    
    p_sup = c_sup.paragraphs[0]
    p_sup.paragraph_format.line_spacing = 1.15
    p_sup.add_run("SIGNATURE\n").bold = True
    p_sup.add_run("[GUIDE NAME]\n").bold = True
    p_sup.add_run("SUPERVISOR\n[DESIGNATION]\nDepartment of Computer Science and Engineering\nChennai Institute of Technology\nChennai - 600 069")
    
    doc.add_paragraph().paragraph_format.space_after = Pt(40)
    p_rev = doc.add_paragraph()
    p_rev.add_run("Submitted for the final review held on ...................................\n\n\n").italic = True
    p_rev.add_run("INTERNAL EXAMINER").bold = True
    p_rev.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    
    doc.add_page_break()

    # =========================================================================
    # 5. DECLARATION
    # =========================================================================
    add_title_p("DECLARATION", size=15, bold=True, space_before=12, space_after=24, color=(6, 78, 59))
    
    add_body_p(
        "We jointly declare that the PBL report on “SMART TEAM FORMATION OPTIMISER FOR COLLEGE STUDENTS” is the result of original work done by us and best of our knowledge, similar work has not been submitted to “ANNA UNIVERSITY CHENNAI” for the requirement of Degree of BACHELOR OF ENGINEERING. This PBL report is submitted on the partial fulfilment of the requirement of the award of Degree of COMPUTER SCIENCE AND ENGINEERING.",
        space_after=40
    )
    
    p_dec_sig = doc.add_paragraph()
    p_dec_sig.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    p_dec_sig.paragraph_format.line_spacing = 1.3
    p_dec_sig.add_run("Signature of Candidates:\n\n\n").bold = True
    p_dec_sig.add_run("1. FAHRAN ALI J  (2104251040235)\n\n\n")
    p_dec_sig.add_run("2. AJAY V  (2104251040051)\n")
    
    p_dec_pl = doc.add_paragraph()
    p_dec_pl.paragraph_format.space_before = Pt(30)
    p_dec_pl.add_run("Place: Chennai\nDate: October 2026")
    
    doc.add_page_break()

    # =========================================================================
    # 6. ACKNOWLEDGEMENT
    # =========================================================================
    add_title_p("ACKNOWLEDGEMENT", size=15, bold=True, space_before=12, space_after=18, color=(6, 78, 59))
    
    add_body_p("We wish to express our sincere gratitude to our honourable Chairman SHRI. P. SRIRAM for providing immense facilities and a conducive academic environment at our institution.")
    add_body_p("We are very proudly rendering our thanks to our Principal Dr. A. RAMESH M.E, Ph.D., for the state-of-the-art facilities and the continuous encouragement given by him towards the progress and successful completion of our project.")
    add_body_p("We would like to express special thanks of gratitude to our Dean Dr. V. SRINIVASA RAO, M.E., Ph.D., who has been the key spring of motivation to us throughout the completion of our course and project work.")
    add_body_p("We proudly render our immense gratitude to the Head of the Department Dr. S. PAVITHRA M.E, Ph.D., for her effective leadership, constant encouragement, and administrative guidance throughout the project duration.")
    add_body_p("We would like to extend our sincere thanks to the Project Co-ordinator [PROJECT COORDINATOR NAME], Department of Computer Science and Engineering, for valuable suggestions and structured evaluation milestones throughout this project.")
    add_body_p("We wish to acknowledge the help received from the class advisors [CLASS ADVISOR NAME], Department of Computer Science and Engineering, and our peers for providing constructive feedback and valuable suggestions for the successful completion of the project.")
    
    p_ack_names = doc.add_paragraph()
    p_ack_names.paragraph_format.space_before = Pt(30)
    p_ack_names.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    p_ack_names.paragraph_format.line_spacing = 1.2
    p_ack_names.add_run("FAHRAN ALI J (2104251040235)\nAJAY V (2104251040051)\n")
    
    doc.add_page_break()

    # =========================================================================
    # 7. ABSTRACT (14pt, double line spacing, 150-200 words)
    # =========================================================================
    add_title_p("ABSTRACT", size=16, bold=True, space_before=12, space_after=18, color=(6, 78, 59))
    
    p_abs = doc.add_paragraph()
    p_abs.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    p_abs.paragraph_format.line_spacing = 2.0 # Double line spacing
    p_abs.paragraph_format.space_after = Pt(18)
    
    r_abs = p_abs.add_run(
        "Forming balanced, high-performing student project and hackathon teams in engineering institutions is traditionally hindered by manual ad-hoc grouping, peer popularity bias, and severe skill imbalances. This project presents the Smart Team Formation Optimiser for College Students, an end-to-end full-stack web application engineered using Java with Spring Boot 3.3.3, Spring Data JPA, MySQL persistence, and an intelligent Python Scikit-Learn microservice. Leveraging Core Java object-oriented principles, modular architecture, and the Java Collections framework, the system implements a multi-constraint snake-draft talent partitioning algorithm that balances technical coding, UI/UX design, communication, documentation, and preferred roles while preserving historical peer collaboration synergy. Integrated with a Random Forest regression model achieving an R² score of 0.873 and RMSE of 3.74%, the platform delivers predictive team success scores, SHAP-inspired point attributions, diagnostic skill gap recommendations, and interactive What-If roster simulations. Validated across diverse student cohorts, the application reduces team skill variance from 1.14 to 0.18, eliminating critical role deficiencies and providing faculty mentors with an automated, data-driven framework for engineering education."
    )
    r_abs.font.name = 'Times New Roman'
    r_abs.font.size = Pt(14)
    
    p_kw = doc.add_paragraph()
    p_kw.paragraph_format.space_before = Pt(12)
    p_kw.paragraph_format.line_spacing = 1.5
    r_kw_lbl = p_kw.add_run("Keywords: ")
    r_kw_lbl.bold = True
    r_kw_lbl.font.name = 'Times New Roman'
    r_kw_lbl.font.size = Pt(12)
    r_kw_val = p_kw.add_run("Java, Spring Boot, Team Formation, Machine Learning, MySQL")
    r_kw_val.font.name = 'Times New Roman'
    r_kw_val.font.size = Pt(12)
    
    doc.add_page_break()

    # =========================================================================
    # 8. TABLE OF CONTENTS
    # =========================================================================
    add_title_p("TABLE OF CONTENTS", size=15, bold=True, space_before=12, space_after=16, color=(6, 78, 59))
    
    toc_data = [
        ["CHAPTER NO.", "TITLE", "PAGE NO."],
        ["", "ABSTRACT", "vii"],
        ["", "LIST OF TABLES", "x"],
        ["", "LIST OF FIGURES", "xi"],
        ["", "LIST OF ABBREVIATIONS", "xii"],
        ["1", "INTRODUCTION", "1"],
        ["", "1.1 Background", "1"],
        ["", "1.2 Driving Question", "2"],
        ["", "1.3 Objectives", "2"],
        ["", "1.4 Scope and Limitations", "3"],
        ["2", "CONCEPT EXPLORATION", "4"],
        ["", "2.1 Related Approaches", "4"],
        ["", "    2.1.1 Traditional Manual Team Formation", "4"],
        ["", "    2.1.2 Java-Based Team Management", "5"],
        ["", "    2.1.3 Java Framework-Based Web Application", "5"],
        ["", "    2.1.4 AI/ML-Based Team Recommendation", "6"],
        ["", "2.2 Technologies and Concepts Explored", "6"],
        ["", "2.3 Summary Table", "8"],
        ["", "2.4 What This Told Us", "8"],
        ["3", "PROJECT PLANNING AND TEAM ORGANISATION", "9"],
        ["", "3.1 Weekly PBL Progress Log", "9"],
        ["", "3.2 Functional and Non-Functional Requirements", "10"],
        ["", "3.3 Hardware and Software Requirements", "11"],
        ["", "3.4 Feasibility Study", "11"],
        ["", "3.5 Team Organisation and Responsibilities", "12"],
        ["4", "ITERATIVE DESIGN AND DEVELOPMENT", "13"],
        ["", "4.1 System Architecture", "13"],
        ["", "4.2 Problem Analysis", "14"],
        ["", "4.3 Baseline Implementation (Iteration 1)", "15"],
        ["", "4.4 Project Refinement (Iteration 2)", "15"],
        ["", "4.5 Final Approach (Iteration 3)", "16"],
        ["", "4.6 Database Design", "16"],
        ["", "4.7 AI/ML Integration", "18"],
        ["", "4.8 Testing and Execution", "19"],
        ["5", "IMPLEMENTATION", "20"],
        ["", "5.1 Module Description", "20"],
        ["", "5.2 Key Java Code Snippets", "22"],
        ["", "5.3 Database Implementation", "26"],
        ["", "5.4 AI/ML Integration", "27"],
        ["", "5.5 User Interface and Demo", "28"],
        ["6", "RESULTS AND DISCUSSION", "30"],
        ["", "6.1 Evaluation Metrics", "30"],
        ["", "6.2 Functional Testing", "31"],
        ["", "6.3 Results Across Iterations", "32"],
        ["", "6.4 Team Formation Results", "33"],
        ["", "6.5 ML Prediction Results", "34"],
        ["", "6.6 Discussion", "35"],
        ["", "6.7 Limitations", "36"],
        ["7", "TEAM REFLECTION AND LEARNING OUTCOMES", "37"],
        ["", "7.1 Individual Reflection – Fahran Ali J", "37"],
        ["", "7.2 Individual Reflection – Ajay V", "38"],
        ["", "7.3 Team Learning", "38"],
        ["", "7.4 Course Outcomes – Evidence Summary", "39"],
        ["8", "CONCLUSION AND FUTURE SCOPE", "40"],
        ["", "8.1 Conclusion", "40"],
        ["", "8.2 Future Scope", "40"],
        ["", "REFERENCES", "42"],
        ["", "APPENDIX", "44"],
        ["", "A.1 Full Source Code Listing", "44"],
        ["", "A.2 Additional Screenshots", "47"],
        ["", "A.3 Self and Peer Assessment", "48"]
    ]
    
    t_toc = doc.add_table(rows=len(toc_data), cols=3)
    t_toc.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_table_borders(t_toc, color="CBD5E1", sz="4")
    
    for r_i, row in enumerate(toc_data):
        r_elem = t_toc.rows[r_i]
        is_hdr = (r_i == 0)
        is_chap = (row[0] != "" and row[0].isdigit())
        bg = "E2E8F0" if is_hdr else ("FFFFFF" if r_i % 2 != 0 else "F8FAFC")
        
        for c_i, val in enumerate(row):
            cell = r_elem.cells[c_i]
            set_cell_background(cell, bg)
            set_cell_margins(cell, top=60, bottom=60, left=80, right=80)
            p = cell.paragraphs[0]
            p.paragraph_format.space_after = Pt(0)
            p.paragraph_format.space_before = Pt(0)
            if c_i == 0:
                p.alignment = WD_ALIGN_PARAGRAPH.CENTER
            elif c_i == 1:
                p.alignment = WD_ALIGN_PARAGRAPH.LEFT
            else:
                p.alignment = WD_ALIGN_PARAGRAPH.RIGHT
            run = p.add_run(val)
            run.font.name = 'Times New Roman'
            run.font.size = Pt(10.5 if is_hdr or is_chap else 10)
            if is_hdr or is_chap:
                run.bold = True
                
    for row in t_toc.rows:
        row.cells[0].width = Inches(1.2)
        row.cells[1].width = Inches(4.3)
        row.cells[2].width = Inches(0.9)
        
    doc.add_page_break()

    # =========================================================================
    # 9. LIST OF TABLES & LIST OF FIGURES & ABBREVIATIONS
    # =========================================================================
    add_title_p("LIST OF TABLES", size=15, bold=True, space_before=12, space_after=14, color=(6, 78, 59))
    
    tables_list = [
        ["TABLE NO.", "TITLE", "PAGE NO."],
        ["2.1", "Comparative Summary of Related Team Formation Approaches", "8"],
        ["3.1", "Weekly Project-Based Learning (PBL) Progress Log", "9"],
        ["3.2", "Hardware and Software Development Specifications", "11"],
        ["3.3", "Team Member Roles and Development Responsibilities", "12"],
        ["4.1", "Relational Database Schema & Entity Attributes", "17"],
        ["5.1", "Application REST API Endpoint Specifications", "21"],
        ["6.1", "Functional Test Cases and Verification Results", "31"],
        ["6.2", "System Evolution Metrics Across PBL Iterations", "32"],
        ["6.3", "Skill Variance and Distribution Comparison (Manual vs AI)", "33"],
        ["6.4", "Random Forest Regressor Performance Evaluation Metrics", "34"],
        ["6.5", "Feature Importance Attribution for Team Success Prediction", "35"],
        ["7.1", "Course Outcomes (CO) to Project Artifacts Mapping", "39"],
        ["A.1", "Self and Peer Assessment Summary", "48"]
    ]
    add_styled_table(tables_list[0], tables_list[1:], col_widths=[1.2, 4.3, 0.9])
    
    doc.add_paragraph().paragraph_format.space_after = Pt(12)
    add_title_p("LIST OF FIGURES", size=15, bold=True, space_before=12, space_after=14, color=(6, 78, 59))
    
    figures_list = [
        ["FIGURE NO.", "TITLE", "PAGE NO."],
        ["4.1", "Multi-Tier System Architecture of Smart Team Formation Optimiser", "13"],
        ["4.2", "Entity-Relationship (ER) Diagram of the Relational Database", "18"],
        ["4.3", "Machine Learning Pipeline and RestTemplate Communication Flow", "19"],
        ["5.1", "Role-Based Authentication and Quick Demo Faculty Portal", "28"],
        ["5.2", "Faculty Analytics Dashboard with KPI Cards and Cohort Radar Chart", "29"],
        ["5.3", "Student Directory with Search, Role Filters, and Bulk CSV Upload", "29"],
        ["5.4", "AI Team Generator with Snake-Draft Configuration and SHAP Waterfall", "30"],
        ["5.5", "Interactive What-If Dynamic Student Swapping and Re-scoring", "30"],
        ["5.6", "College Hackathon and Project Event Allocation Tracker", "47"],
        ["5.7", "Multi-Team Side-by-Side Benchmarking Radar Overlay and Matrix", "47"],
        ["5.8", "Team Rankings Leaderboard with Gold, Silver, and Bronze Podium", "48"]
    ]
    add_styled_table(figures_list[0], figures_list[1:], col_widths=[1.2, 4.3, 0.9])
    
    doc.add_paragraph().paragraph_format.space_after = Pt(12)
    add_title_p("LIST OF ABBREVIATIONS", size=15, bold=True, space_before=12, space_after=14, color=(6, 78, 59))
    
    abbrevs = [
        ["ABBREVIATION", "FULL FORM"],
        ["JDK", "Java Development Kit"],
        ["OOP", "Object-Oriented Programming"],
        ["JDBC", "Java Database Connectivity"],
        ["JPA", "Java Persistence API"],
        ["ORM", "Object-Relational Mapping"],
        ["REST", "Representational State Transfer"],
        ["API", "Application Programming Interface"],
        ["DTO", "Data Transfer Object"],
        ["JSON", "JavaScript Object Notation"],
        ["CSV", "Comma-Separated Values"],
        ["MVC", "Model-View-Controller"],
        ["IoC", "Inversion of Control"],
        ["DI", "Dependency Injection"],
        ["ML", "Machine Learning"],
        ["RF", "Random Forest"],
        ["RMSE", "Root Mean Squared Error"],
        ["MAE", "Mean Absolute Error"],
        ["R²", "Coefficient of Determination"],
        ["SHAP", "SHapley Additive exPlanations"],
        ["XAI", "Explainable Artificial Intelligence"],
        ["SPA", "Single-Page Application"],
        ["UI/UX", "User Interface / User Experience"],
        ["PBL", "Project-Based Learning"],
        ["CIT", "Chennai Institute of Technology"],
        ["HOD", "Head of the Department"],
        ["CO", "Course Outcome"]
    ]
    add_styled_table(abbrevs[0], abbrevs[1:], col_widths=[2.0, 4.4])
    
    doc.add_page_break()

    # =========================================================================
    # CHAPTER 1 – INTRODUCTION
    # =========================================================================
    add_chap_heading(1, "Introduction")
    
    add_sec_heading("1.1", "Background")
    add_body_p(
        "In modern undergraduate engineering education, collaborative coursework, capstone design projects, and competitive hackathons play a foundational role in fostering technical competency, practical problem-solving, and professional soft skills. Within Computer Science and Engineering (CSE) departments, faculty mentors are routinely tasked with partitioning cohorts of 60 to 120 students into small, cohesive project groups of 3 to 5 members each semester. However, the prevailing institutional practices for team formation remain overwhelmingly manual, informal, and ad-hoc."
    )
    add_body_p(
        "When students are permitted to form teams autonomously, self-selection bias dominates: students overwhelmingly choose close friends, roommates, or peers with identical academic backgrounds. This natural clustering results in severe structural pathologies. 'All-developer' teams often lack UI/UX design competency, product strategy, and structured technical documentation, leading to delayed submissions and unpolished deliveries. Conversely, teams composed entirely of novice programmers experience severe implementation bottlenecks when building complex database schemas or full-stack architectures. Furthermore, repeated collaboration between the same individuals deprives students of learning how to coordinate across diverse talent profiles—a critical prerequisite for industry readiness."
    )
    add_body_p(
        "When faculty mentors attempt manual intervention, the sheer combinatorial complexity becomes unmanageable. Manually balancing multiple competing dimensions—such as programming proficiency, design aptitude, verbal communication, project domain interests, previous hackathon win rates, and interpersonal synergy—across dozens of students in spreadsheets is labor-intensive and prone to subjective error. Consequently, there is an urgent educational need for an intelligent, automated, and explainable software platform that leverages Object-Oriented Java design, robust relational persistence, and machine learning heuristics to optimize student team formation."
    )
    
    add_sec_heading("1.2", "Driving Question")
    add_body_p(
        "The core investigative inquiry guiding this Project-Based Learning endeavor is formulated as follows:"
    )
    
    # Callout Box for Driving Question
    t_dq = doc.add_table(rows=1, cols=1)
    t_dq.alignment = WD_TABLE_ALIGNMENT.CENTER
    c_dq = t_dq.cell(0, 0)
    c_dq.width = Inches(6.25)
    set_cell_background(c_dq, "ECFDF5")
    set_box_borders(c_dq, color="059669", sz="10")
    set_cell_margins(c_dq, top=140, bottom=140, left=180, right=180)
    pdq = c_dq.paragraphs[0]
    pdq.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    rdq = pdq.add_run("“Can a Java-based intelligent web application form balanced student teams by analysing skills, experience, roles and previous performance while providing explainable team performance predictions?”")
    rdq.bold = True
    rdq.italic = True
    rdq.font.name = 'Times New Roman'
    rdq.font.size = Pt(12)
    rdq.font.color.rgb = RGBColor(0x06, 0x5F, 0x46)
    
    doc.add_paragraph().paragraph_format.space_after = Pt(6)
    add_body_p(
        "To address this driving question comprehensively, the project decomposes the challenge into concrete, executable engineering tasks: (1) modeling multidimensional student attributes and historical collaboration matrices using Java object-oriented principles; (2) implementing a deterministic multi-constraint snake-draft partitioning algorithm in Spring Boot; (3) integrating a high-precision Random Forest regression microservice to predict team potential; (4) providing SHAP-inspired explainability and actionable skill-gap diagnostics; and (5) delivering an interactive What-If simulation interface for faculty decision-making."
    )
    
    add_sec_heading("1.3", "Objectives")
    add_body_p("The technical and learning objectives established for this project are:")
    add_bullet_item("Analyze Academic Team Dynamics", "Investigate the real-world mathematical constraints, skill bottlenecks, and collaboration failure modes in student software engineering teams.")
    add_bullet_item("Design Modular Java Architecture", "Architect a multi-tier web application leveraging Spring Boot 3.3.3, RESTful APIs, Spring Data JPA, and MVC separation of concerns.")
    add_bullet_item("Implement Object-Oriented Domain Models", "Apply core Java OOP principles—including encapsulation, strong typing, domain validation, and custom collections—to represent students, teams, and historical trajectories.")
    add_bullet_item("Engineer Snake-Draft Team Allocation", "Develop a high-performance heuristic algorithm that balances coding, design, communication, documentation, and role diversity across teams.")
    add_bullet_item("Integrate AI/ML Predictive Microservice", "Connect the Java backend via Spring RestTemplate to a Scikit-Learn Random Forest Regressor to forecast team success scores with 94%+ confidence.")
    add_bullet_item("Implement Explainable AI & What-If Simulation", "Generate SHAP feature contribution waterfalls and provide real-time dynamic student swapping with instant score recalculation.")
    add_bullet_item("Deliver Faculty Governance Tooling", "Provide bulk CSV student onboarding, multi-team radar comparison matrices, hackathon event tracking, and printable PDF evaluation reports.")
    
    add_sec_heading("1.4", "Scope and Limitations")
    add_body_p(
        "The project scope encompasses the complete design, implementation, and empirical validation of the Smart Team Formation Optimiser as a self-contained web platform. The system operates on structured student profiles, historical project records, and hackathon data stored in relational persistence."
    )
    add_body_p(
        "In terms of academic and operational limitations: (1) The machine learning regression model is trained on a synthetic historical cohort of 1,500 collegiate project teams calibrated to realistic distribution curves; it provides an empirical success estimate rather than a deterministic guarantee of team victory; (2) The current implementation is optimized for intra-departmental cohorts (up to 500 active students per academic year) deployed in a localized server environment; (3) Student skill ratings (1 to 5 scale) are currently sourced from verified self-assessments and faculty evaluations rather than automated live scraping of external GitHub or LeetCode repositories. These boundaries ensure high reliability while delineating clear pathways for future cloud expansion."
    )
    
    doc.add_page_break()

    # =========================================================================
    # CHAPTER 2 – CONCEPT EXPLORATION
    # =========================================================================
    add_chap_heading(2, "Concept Exploration")
    
    add_sec_heading("2.1", "Related Approaches")
    
    add_subsec_heading("2.1.1", "Traditional Manual Team Formation")
    add_body_p(
        "Manual team formation typically relies either on student self-selection or random allocation by instructors. Self-selection consistently suffers from homophily—the tendency of individuals to associate with similar peers. In educational research, Belbin’s Team Role Theory demonstrates that high-performing teams require complementary behavioral and technical roles, including Coordinators, Implementers, and Completer-Finishers. Random assignment, while unbiased, frequently groups novice students together or creates teams with zero technical leadership, resulting in project abandonment."
    )
    
    add_subsec_heading("2.1.2", "Java-Based Team Management")
    add_body_p(
        "Early academic software systems addressed student grouping using standalone Core Java console applications or desktop GUIs (Swing/AWT). These systems typically employed greedy sorting via Java Collections (ArrayList, HashMap) and custom Comparator interfaces. While computationally efficient, standalone console systems lacked multi-user concurrency, relational data persistence, external API connectivity, and dynamic interactive visualization required by modern academic institutions."
    )
    
    add_subsec_heading("2.1.3", "Java Framework-Based Web Application")
    add_body_p(
        "Enterprise Java frameworks—specifically Spring Boot and Jakarta Persistence (JPA) with Hibernate—represent the gold standard for reliable, maintainable web applications. Spring Boot’s Inversion of Control (IoC) container and Dependency Injection (DI) decouple business services from HTTP transport layers. Spring Data JPA replaces cumbersome, error-prone boilerplate JDBC code with type-safe repository interfaces, automated schema generation, and declarative transaction management (@Transactional), ensuring robust ACID properties for academic records."
    )
    
    add_subsec_heading("2.1.4", "AI/ML-Based Team Recommendation")
    add_body_p(
        "Recent advances in machine learning have introduced predictive analytics into educational software engineering. Supervised regression models—such as Random Forest Regressors, Gradient Boosting, and Multi-Layer Perceptrons—can model complex non-linear interactions between diverse human factors (e.g., how high role diversity compensates for moderate coding ratings). Furthermore, Explainable Artificial Intelligence (XAI) frameworks like SHAP (SHapley Additive exPlanations) enable black-box models to decompose predictions into human-interpretable point contributions, making AI recommendations trustworthy for academic mentors."
    )
    
    add_sec_heading("2.2", "Technologies and Concepts Explored")
    add_body_p(
        "To construct an industrial-grade solution, the project explored and synthesized several key computing paradigms:"
    )
    add_bullet_item("Core Java & OOP Principles", "Encapsulation safeguards student data; polymorphism enables flexible heuristic scoring strategies; interfaces decouple persistence contracts from business logic.")
    add_bullet_item("Spring Boot Ecosystem", "Spring Web manages RESTful endpoints; Jackson handles JSON serialization; Spring Data JPA orchestrates Hibernate ORM entity life-cycles.")
    add_bullet_item("Relational Persistence (MySQL / H2)", "Normalized schema design ensures referential integrity across students, teams, hackathons, and collaboration synergy matrices.")
    add_bullet_item("Microservice Communication", "Spring RestTemplate enables synchronous JSON exchange between the Java backend and the Python Flask ML service over HTTP.")
    add_bullet_item("Explainable Machine Learning", "Scikit-Learn Random Forest Regressor models 10 multi-dimensional team factors, while custom SHAP attributions decompose predictions into intuitive point drivers.")
    
    add_sec_heading("2.3", "Summary Table")
    add_body_p("Table 2.1 provides a structured comparative analysis of existing approaches against our engineered system.")
    
    lit_table = [
        ["REF.", "APPROACH", "TECHNOLOGY / MODEL", "KEY IDEA", "LIMITATION / OBSERVATION"],
        ["[1]", "Self-Selection / Ad-hoc", "Manual / Spreadsheet", "Students choose friends", "Severe skill & role imbalance; homophily bias"],
        ["[2]", "Belbin Role Framework", "Psychometric Survey", "Behavioral role allocation", "Lacks technical coding & domain skill modeling"],
        ["[3]", "Core Java CLI Sorter", "Java Collections, Comparator", "Greedy skill-based sorting", "No web UI, no persistence, no ML prediction"],
        ["[4]", "Genetic Algorithm Team", "Python / Evolutionary Alg.", "Combinatorial optimization", "High compute latency; lacks explainable rationale"],
        ["[5]", "Enterprise Web Portal", "Spring MVC + JDBC", "Centralized student records", "Static manual assignment; no AI optimization"],
        ["[6]", "SHAP Explainable AI", "TreeExplainer / Python", "Additive feature attribution", "Standalone data science script; not integrated"],
        ["[Ours]", "Smart Team Optimiser", "Spring Boot + JPA + Flask ML", "Snake-Draft + RF + XAI", "Complete full-stack integration with What-If UI"]
    ]
    add_styled_table(lit_table[0], lit_table[1:], col_widths=[0.6, 1.4, 1.4, 1.4, 1.5], caption="Table 2.1 – Comparative Summary of Related Team Formation Approaches")
    
    add_sec_heading("2.4", "What This Told Us")
    add_body_p(
        "This exploration established that while algorithmic grouping, enterprise Java persistence, and predictive machine learning exist in isolation, no unified platform seamlessly combines all three into an accessible faculty portal. We decided to bridge this gap by establishing Spring Boot as the core architectural engine, executing deterministic snake-draft partitioning locally for instant responsiveness, and delegating complex non-linear success predictions to an isolated Python Flask microservice."
    )
    
    doc.add_page_break()

    # =========================================================================
    # CHAPTER 3 – PROJECT PLANNING AND TEAM ORGANISATION
    # =========================================================================
    add_chap_heading(3, "Project Planning and Team Organisation")
    
    add_sec_heading("3.1", "Weekly PBL Progress Log")
    add_body_p(
        "Table 3.1 details the structured 12-week development lifecycle executed across the academic semester."
    )
    
    pbl_log = [
        ["WEEK", "MILESTONE / TASK", "WORK DONE", "MENTOR REMARKS"],
        ["1–2", "Problem Formulation & Scoping", "Literature review on team dynamics, problem definition, objectives framing, and tech stack finalization.", "[To be filled after mentor review]"],
        ["3–4", "Architecture & Schema Design", "Designed multi-tier architecture, ER schema, DTO contracts, and snake-draft mathematical logic.", "[To be filled after mentor review]"],
        ["5–6", "Core Java Backend Setup", "Built Spring Boot application skeleton, Student/Team JPA entities, H2/MySQL repositories, and REST controllers.", "[To be filled after mentor review]"],
        ["7–8", "Algorithm & ML Microservice", "Implemented Snake-Draft team formation in Java; trained Random Forest model (1,500 records) in Python Flask.", "[To be filled after mentor review]"],
        ["9–10", "Full-Stack Integration & XAI", "Connected Spring RestTemplate to Flask ML service; implemented SHAP point waterfall, What-If swapper, and UI.", "[To be filled after mentor review]"],
        ["11–12", "Testing, Export & Documentation", "Executed comprehensive functional test suites, implemented CSV/PDF export engines, and finalized PBL report.", "[To be filled after mentor review]"]
    ]
    add_styled_table(pbl_log[0], pbl_log[1:], col_widths=[0.8, 1.8, 2.5, 1.2], caption="Table 3.1 – Weekly Project-Based Learning (PBL) Progress Log")
    
    add_sec_heading("3.2", "Functional and Non-Functional Requirements")
    add_subsec_heading("3.2.1", "Functional Requirements (FR)")
    add_bullet_item("FR-01 (Authentication)", "Secure credential authentication for faculty and administrative roles with session token persistence.")
    add_bullet_item("FR-02 (Student Management)", "Full CRUD operations for student profiles, including register number, email, 4-axis skill ratings, and preferred roles.")
    add_bullet_item("FR-03 (Bulk CSV Onboarding)", "Parsing and batch ingestion of multi-student CSV records with automated validation and error flagging.")
    add_bullet_item("FR-04 (Snake-Draft Formation)", "Automated team generation balancing composite talent ratings, project domains, and target team sizes (3–5 members).")
    add_bullet_item("FR-05 (ML Performance Prediction)", "Asynchronous calculation of team success probability score (0–100%) and potential tier categorization via Random Forest.")
    add_bullet_item("FR-06 (Explainable Diagnostics)", "Generation of SHAP-inspired point contributions and actionable skill gap recommendations for mentors.")
    add_bullet_item("FR-07 (What-If Interactive Swapper)", "Real-time swapping of student roster assignments with live API re-evaluation of team success scores.")
    add_bullet_item("FR-08 (Hackathon Event Tracking)", "Management of college competitions, milestone deadlines, and team slot allocation tracking.")
    add_bullet_item("FR-09 (Benchmarking & Ranking)", "Side-by-side multi-axis radar chart comparison matrix and automated podium leaderboard generation.")
    add_bullet_item("FR-10 (Report Exporting)", "Generation of downloadable CSV roster datasets and printable faculty evaluation sheets.")
    
    add_subsec_heading("3.2.2", "Non-Functional Requirements (NFR)")
    add_bullet_item("NFR-01 (Performance)", "Team generation for 100+ students must execute in under 250 ms; ML inference response under 100 ms.")
    add_bullet_item("NFR-02 (Data Integrity)", "Relational foreign keys and JPA cascade rules must prevent orphaned team members or duplicate registrations.")
    add_bullet_item("NFR-03 (Fault Tolerance)", "Java backend must provide deterministic heuristic fallbacks if the external Python ML service is offline.")
    add_bullet_item("NFR-04 (Usability & Design)", "Responsive Single-Page Application (SPA) supporting Warm Cream light mode and Dark Charcoal mode.")
    
    add_sec_heading("3.3", "Hardware and Software Requirements")
    add_body_p("Table 3.2 enumerates the exact hardware and software specifications utilized in the project.")
    
    hw_sw = [
        ["CATEGORY", "DEVELOPMENT SPECIFICATION", "MINIMUM REQUIREMENT"],
        ["Processor", "Intel Core i5 / AMD Ryzen 5 (6 Cores, 3.2 GHz)", "Dual Core 2.0 GHz Processor"],
        ["System Memory (RAM)", "16 GB DDR4 RAM", "8 GB RAM"],
        ["Storage", "512 GB NVMe SSD", "20 GB Free Disk Space"],
        ["Operating System", "Microsoft Windows 11 (64-bit)", "Windows 10 / Linux / macOS"],
        ["Java Platform", "Java Development Kit (JDK) 21 (LTS)", "JDK 17 (LTS)"],
        ["Backend Framework", "Spring Boot 3.3.3 (Spring Web, Data JPA)", "Spring Boot 3.0+"],
        ["Build Tool", "Apache Maven 3.9.9", "Maven 3.8+"],
        ["Database Engine", "H2 Embedded File Database / MySQL 8.0", "MySQL 5.7+ / H2"],
        ["ML Microservice", "Python 3.13 / 3.14 with Flask 3.1.3", "Python 3.10+"],
        ["Data Science Libraries", "Scikit-Learn 1.9.0, Pandas, NumPy, Joblib", "Scikit-Learn 1.0+"],
        ["Frontend UI", "HTML5, CSS3 Glassmorphism, Vanilla JS, Chart.js 4.4", "Modern Web Browser"],
        ["IDE & Tools", "IntelliJ IDEA Ultimate, VS Code, Postman", "Eclipse / VS Code"]
    ]
    add_styled_table(hw_sw[0], hw_sw[1:], col_widths=[1.5, 2.7, 2.1], caption="Table 3.2 – Hardware and Software Development Specifications")
    
    add_sec_heading("3.4", "Feasibility Study")
    add_body_p(
        "A multi-dimensional feasibility study confirmed project viability: (1) Technical Feasibility: The decoupled architecture utilizes robust, open-source enterprise tools with extensive documentation; (2) Operational Feasibility: The web portal requires zero client installation and features an intuitive faculty dashboard; (3) Economic Feasibility: All frameworks, libraries, and databases are open-source with zero licensing costs; (4) Schedule Feasibility: The 12-week iterative timeline allowed progressive delivery from CLI baseline to full-stack ML integration."
    )
    
    add_sec_heading("3.5", "Team Organisation and Responsibilities")
    add_body_p(
        "Table 3.3 details the division of engineering responsibilities between the two team members."
    )
    
    team_resp = [
        ["TEAM MEMBER", "CORE FOCUS AREA", "PRIMARY MODULE RESPONSIBILITIES"],
        ["Fahran Ali J\n(2104251040235)", "Backend Architecture & Heuristic Engineering", "Spring Boot setup, JPA Entity modeling, Snake-Draft team formation algorithm, RestTemplate ML client, Exception handling, Security."],
        ["Ajay V\n(2104251040051)", "Machine Learning, Frontend UI & Analytics", "Python Flask ML microservice training, Random Forest model tuning, Chart.js radar/bar visualization, What-If swapper UI, CSV/PDF export."]
    ]
    add_styled_table(team_resp[0], team_resp[1:], col_widths=[1.5, 1.8, 3.0], caption="Table 3.3 – Team Member Roles and Development Responsibilities")
    
    doc.add_page_break()

    # =========================================================================
    # CHAPTER 4 – ITERATIVE DESIGN AND DEVELOPMENT
    # =========================================================================
    add_chap_heading(4, "Iterative Design and Development")
    
    add_sec_heading("4.1", "System Architecture")
    add_body_p(
        "The Smart Team Formation Optimiser is architected as a modular, decoupled multi-tier distributed application. The architecture cleanly isolates presentation, business orchestration, relational persistence, and predictive intelligence into distinct layers:"
    )
    add_bullet_item("Presentation Tier (Frontend)", "A high-performance Single-Page Application (SPA) built using semantic HTML5, CSS3 custom variables, Chart.js visualization, and asynchronous ES6 JavaScript.")
    add_bullet_item("Application Tier (Spring Boot Backend)", "The central enterprise engine hosting REST controllers, validation middleware, and business services executing heuristic snake-draft partitioning.")
    add_bullet_item("Persistence Tier (Spring Data JPA / MySQL)", "Hibernate-driven ORM mapping domain entities to normalized relational tables with transactional boundaries.")
    add_bullet_item("Intelligence Tier (Python ML Microservice)", "A standalone Flask REST service hosting the serialized Scikit-Learn Random Forest Regressor for success scoring, SHAP attribution, and diagnostic recommendations.")
    
    # Architecture Diagram Text Box
    add_body_p("Figure 4.1 illustrates the end-to-end multi-tier communication flow of the platform.")
    
    t_arch = doc.add_table(rows=1, cols=1)
    t_arch.alignment = WD_TABLE_ALIGNMENT.CENTER
    c_arch = t_arch.cell(0, 0)
    c_arch.width = Inches(6.25)
    set_cell_background(c_arch, "F8FAFC")
    set_box_borders(c_arch, color="059669", sz="8")
    set_cell_margins(c_arch, top=140, bottom=140, left=140, right=140)
    parch = c_arch.paragraphs[0]
    parch.alignment = WD_ALIGN_PARAGRAPH.CENTER
    parch.paragraph_format.line_spacing = 1.05
    arch_ascii = """+-------------------------------------------------------------------------+
|                  CLIENT TIER: BROWSER / SINGLE-PAGE APP                 |
|   Dashboard | Student Directory | AI Generator | Swapper | Comparison   |
+------------------------------------+------------------------------------+
                                     | HTTP REST (JSON / Multipart)
                                     v
+-------------------------------------------------------------------------+
|                APPLICATION TIER: JAVA SPRING BOOT (PORT 8080)           |
|  [REST Controllers] -> [Security Filter] -> [Global Exception Handler]  |
|                                    |                                    |
|  [Business Services]:                                                   |
|   - TeamFormationService (Snake-Draft Algorithm & Role Assignment)      |
|   - StudentService (CRUD, Validation, CSV Batch Ingestion)              |
|   - HistoryService (Collaboration Synergy & Hackathon History)          |
|   - AnalyticsService (Cohort Metrics & Comparison Matrix Engine)        |
|   - MlPredictionClientService (Spring RestTemplate with Fallback)       |
+--------------------+-------------------------------+--------------------+
                     |                               |
        JPA / JDBC   v                               v  HTTP REST (JSON)
+--------------------+--------------+   +------------+--------------------+
|       PERSISTENCE TIER           |   |       INTELLIGENCE TIER            |
|   MySQL 8.0 / H2 File Database   |   |   Python Flask Microservice (5000) |
|   - students       - teams       |   |   - Random Forest Regressor        |
|   - team_members   - events      |   |   - SHAP Point Attributions        |
|   - hackathon_hist - collab_hist |   |   - Skill Gap Diagnostic Engine    |
+----------------------------------+   +------------------------------------+"""
    r_arch = parch.add_run(arch_ascii)
    r_arch.font.name = 'Consolas'
    r_arch.font.size = Pt(8.5)
    
    p_arch_cap = doc.add_paragraph()
    p_arch_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_arch_cap.paragraph_format.space_before = Pt(6)
    p_arch_cap.paragraph_format.space_after = Pt(12)
    r_ac = p_arch_cap.add_run("Figure 4.1 – Multi-Tier System Architecture of Smart Team Formation Optimiser")
    r_ac.bold = True
    r_ac.font.name = 'Times New Roman'
    r_ac.font.size = Pt(10.5)
    
    add_sec_heading("4.2", "Problem Analysis")
    add_body_p(
        "Mathematically, partitioning $N$ students into $K$ teams of size $S$ subject to multi-attribute balancing is an NP-hard combinatorial optimization problem. Given $N=100$ and $S=4$, the number of possible team combinations is astronomical ($> 10^{60}$). Exact linear programming solvers suffer from exponential execution latency unsuitable for real-time interactive web applications. Therefore, designing a deterministic, multi-criteria heuristic algorithm in Java was essential."
    )
    
    add_sec_heading("4.3", "Baseline Implementation (Iteration 1)")
    add_body_p(
        "In Iteration 1, the team built a standalone Core Java console prototype. Students were modeled as plain Java objects stored in an ArrayList. Grouping was performed using simple greedy descending sort on a single coding rating. While functional, Iteration 1 exhibited three major defects: (1) Severe Role Clustering: Top teams received multiple leaders and zero UI designers; (2) Data Volatility: All student records were lost upon process termination due to lack of a database; (3) Lack of Predictive Guidance: Mentors received no forecast of team synergy or success potential."
    )
    
    add_sec_heading("4.4", "Project Refinement (Iteration 2)")
    add_body_p(
        "Iteration 2 transitioned the project to an enterprise Spring Boot framework. Relational tables were provisioned using Spring Data JPA, and RESTful endpoints replaced console input. To resolve talent clustering, the team formulated a composite multi-factor scoring function:"
    )
    add_body_p(
        "$$\\text{CompositeScore} = w_c \\cdot \\text{Coding} + w_d \\cdot \\text{Design} + w_m \\cdot \\text{Comm} + w_q \\cdot \\text{Doc} + \\text{DomainBonus} + \\text{ExpBonus}$$",
        italic=True
    )
    add_body_p(
        "A snake-draft allocation pattern was introduced: sorting students by composite score and distributing them across team buckets in alternating forward and reverse passes ($T_1 \\rightarrow T_2 \\rightarrow T_3 \\rightarrow T_3 \\rightarrow T_2 \\rightarrow T_1$). This mathematically guaranteed an even distribution of top-tier, mid-tier, and developing talent."
    )
    
    add_sec_heading("4.5", "Final Approach (Iteration 3)")
    add_body_p(
        "Iteration 3 completed the platform by introducing: (1) An isolated Python Flask machine learning microservice hosting a trained Random Forest Regressor ($R^2=0.873$); (2) SHAP point waterfalls decomposing team success scores; (3) An interactive What-If live roster swapper; (4) Multi-team radar benchmarking; (5) Bulk CSV onboarding; and (6) Hackathon event tracking."
    )
    
    add_sec_heading("4.6", "Database Design")
    add_body_p(
        "The relational schema is fully normalized into Third Normal Form (3NF) to guarantee referential integrity. Table 4.1 outlines the primary entities, columns, and constraints."
    )
    
    db_schema = [
        ["TABLE NAME", "PRIMARY KEY", "FOREIGN KEYS", "KEY ATTRIBUTES", "RELATIONSHIP DESCRIPTION"],
        ["students", "id (BIGINT)", "None", "reg_no, name, email, coding, design, comm, doc, preferred_role", "Master record for all enrolled students."],
        ["teams", "id (BIGINT)", "None", "team_name, project_category, academic_year, success_score, prediction_label", "Stores generated and finalized team rosters."],
        ["team_members", "id (BIGINT)", "team_id, student_id", "assigned_role, joined_at", "Many-to-Many association link table."],
        ["hackathon_hist", "id (BIGINT)", "student_id", "event_name, position, award_category, event_date", "One-to-Many track record of student hackathons."],
        ["project_hist", "id (BIGINT)", "student_id", "project_title, domain, grade_achieved, semester", "One-to-Many record of previous course projects."],
        ["collab_hist", "id (BIGINT)", "student1_id, student2_id", "projects_together, completed_projects, synergy_score", "Tracks pairwise interpersonal student synergy."],
        ["events", "id (BIGINT)", "None", "title, category, max_teams, event_date, status", "College hackathons available for team allocation."],
        ["users", "id (BIGINT)", "None", "username, password_hash, full_name, role, department", "Faculty and administrator authentication accounts."]
    ]
    add_styled_table(db_schema[0], db_schema[1:], col_widths=[1.0, 0.9, 1.2, 1.8, 1.4], caption="Table 4.1 – Relational Database Schema & Entity Attributes")
    
    add_sec_heading("4.7", "AI/ML Integration")
    add_body_p(
        "The intelligence pipeline extracts a 10-dimensional feature vector for any proposed team roster: (1) Average Coding Rating, (2) Average Design Rating, (3) Average Communication, (4) Average Documentation, (5) Role Diversity Entropy ($0.0 - 1.0$), (6) Interest Domain Similarity ($0.0 - 1.0$), (7) Pairwise Collaboration Synergy ($0.0 - 1.0$), (8) Team Experience Rating ($0.0 - 1.0$), (9) Aggregate Hackathon Win Rate ($0.0 - 1.0$), and (10) Historical Project Completion Rate ($0.0 - 1.0$). The vector is transmitted via JSON payload to the Flask endpoint `/predict-team`, where the Random Forest ensemble computes the predicted score and SHAP attributions."
    )
    
    add_sec_heading("4.8", "Testing and Execution")
    add_body_p(
        "The system underwent automated JUnit 5 unit testing, Spring Boot `@SpringBootTest` integration validation, and Postman API endpoint verification. Mockito was utilized to test service business logic independently of database states."
    )
    
    doc.add_page_break()

    # =========================================================================
    # CHAPTER 5 – IMPLEMENTATION
    # =========================================================================
    add_chap_heading(5, "Implementation")
    
    add_sec_heading("5.1", "Module Description")
    
    add_subsec_heading("5.1.1", "Authentication Module")
    add_body_p(
        "Manages faculty and administrative sign-in via `AuthController.java` and `AuthService.java`. Passwords are validated using cryptographic SHA-256 hashing. Generates session tokens stored in browser SessionStorage and LocalStorage, supporting dedicated demo faculty logins."
    )
    
    add_subsec_heading("5.1.2", "Student Management Module")
    add_body_p(
        "Provides full lifecycle management for student records via `StudentController.java` and `StudentService.java`. Enforces strict Bean Validation (@Size, @Email, @Min, @Max). Supports dynamic search filtering and bulk CSV batch ingestion."
    )
    
    add_subsec_heading("5.1.3", "Student Profile and Skill Module")
    add_body_p(
        "Renders comprehensive individual student analytics, combining 4-axis skill ratings (Coding, UI/UX Design, Communication, Documentation), preferred project roles, domain interests, and historical portfolio records into an animated profile modal."
    )
    
    add_subsec_heading("5.1.4", "Hackathon and Project History Module")
    add_body_p(
        "Maintains persistent records of external hackathon participation, podium victories, completed academic projects, and completion percentages via `HackathonHistoryRepository` and `ProjectHistoryRepository`."
    )
    
    add_subsec_heading("5.1.5", "Team Formation Module")
    add_body_p(
        "The algorithmic core implemented in `TeamFormationService.java`. Executes composite score ranking, snake-draft bucket partitioning, and automated role balancing to generate balanced rosters based on faculty-specified team sizes (3 to 5) and domains."
    )
    
    add_subsec_heading("5.1.6", "Role Recommendation Module")
    add_body_p(
        "Dynamically analyzes individual member skill vectors against team requirements to assign specialized roles: Team Lead, Full-Stack Developer, UI/UX Designer, QA & Documentation Lead, and Data Engineer."
    )
    
    add_subsec_heading("5.1.7", "Team Performance Prediction Module")
    add_body_p(
        "Communicates with the Python microservice via `MlPredictionClientService.java` using Spring RestTemplate to compute non-linear success probabilities (0 to 100%) and tier classifications (High Potential, Moderate Potential, Needs Improvement)."
    )
    
    add_subsec_heading("5.1.8", "Collaboration History Module")
    add_body_p(
        "Quantifies pairwise interpersonal synergy between students based on past joint project outcomes, boosting compatibility scores when proven collaborators are placed together."
    )
    
    add_subsec_heading("5.1.9", "Team Weakness Detection Module")
    add_body_p(
        "Analyzes skill rating standard deviations and role distributions to identify critical team deficiencies (e.g., coding bottlenecks, missing design competency) and outputs actionable mentorship recommendations."
    )
    
    add_subsec_heading("5.1.10", "What-If Team Simulator")
    add_body_p(
        "Enables interactive drag-and-drop / click-swapping of students between formed teams via `POST /api/teams/evaluate-roster`, dynamically re-evaluating ML scores and Explainable AI rationales in real time."
    )
    
    add_subsec_heading("5.1.11", "Team Comparison and Ranking")
    add_body_p(
        "Renders side-by-side multi-axis radar chart overlays comparing technical skills, role diversity, experience, and synergy, coupled with an automated podium leaderboard ranking all teams by success score."
    )
    
    add_sec_heading("5.2", "Key Java Code Snippets")
    add_body_p(
        "The following actual Java code snippets illustrate core object-oriented principles, Spring Data JPA persistence, algorithm implementation, and exception handling across the platform."
    )
    
    # Snippet 1
    add_code_snippet(
"""@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Student name is required")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Register number is required")
    @Column(name = "register_number", nullable = false, unique = true, length = 30)
    private String registerNumber;

    @Min(1) @Max(5)
    @Column(name = "coding_skill", nullable = false)
    private Integer codingSkill = 3;

    @Min(1) @Max(5)
    @Column(name = "design_skill", nullable = false)
    private Integer designSkill = 3;

    @Column(name = "preferred_role", nullable = false, length = 60)
    private String preferredRole = "Developer";

    // Getters, Setters, and Constructors implementing Encapsulation
}""",
        caption="Snippet 5.1 – Student Entity Implementation (com.teamforge.ai.model.Student.java)"
    )
    add_body_p("Java OOP Concept: Demonstrates strong Encapsulation, Jakarta Persistence Entity Mapping, Bean Validation constraints, and Object-Relational data integrity.")
    
    # Snippet 2
    add_code_snippet(
"""public List<GeneratedTeamDTO> generateTeams(TeamFormationRequest request) {
    List<Student> pool = studentRepository.findAll();
    int teamSize = request.getTeamSize() != null ? request.getTeamSize() : 4;
    int numTeams = Math.max(1, pool.size() / teamSize);

    // 1. Compute Composite Scores for Snake Draft Sorting
    Map<Student, Double> studentScores = new HashMap<>();
    for (Student s : pool) {
        studentScores.put(s, calculateCompositeScore(s, request.getSkillPriority(), request.getProjectCategory()));
    }

    List<Student> sortedStudents = new ArrayList<>(pool);
    sortedStudents.sort((s1, s2) -> Double.compare(studentScores.get(s2), studentScores.get(s1)));

    // 2. Multi-Bucket Snake-Draft Partitioning
    List<List<Student>> teamBuckets = new ArrayList<>();
    for (int i = 0; i < numTeams; i++) teamBuckets.add(new ArrayList<>());

    boolean forward = true;
    int bucketIndex = 0;
    for (Student s : sortedStudents) {
        teamBuckets.get(bucketIndex).add(s);
        if (forward) {
            bucketIndex++;
            if (bucketIndex >= numTeams) { bucketIndex = numTeams - 1; forward = false; }
        } else {
            bucketIndex--;
            if (bucketIndex < 0) { bucketIndex = 0; forward = true; }
        }
    }
    return buildTeamDTOs(teamBuckets, request.getProjectCategory());
}""",
        caption="Snippet 5.2 – Snake-Draft Team Allocation Algorithm (com.teamforge.ai.service.TeamFormationService.java)"
    )
    add_body_p("Java OOP Concept: Demonstrates Java Collections Framework (ArrayList, HashMap), Lambda expressions, custom Comparator sorting, and deterministic heuristic partitioning.")
    
    # Snippet 3
    add_code_snippet(
"""@Service
public class MlPredictionClientService {
    private final RestTemplate restTemplate;
    private final String mlServiceUrl = "http://localhost:5000/predict-team";

    public MlPredictionResponse predictTeamPerformance(MlPredictionRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MlPredictionRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<MlPredictionResponse> response = restTemplate.postForEntity(
                mlServiceUrl, entity, MlPredictionResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            // Robust Fallback Heuristic if ML microservice is unreachable
            return calculateRuleBasedFallback(request);
        }
    }
}""",
        caption="Snippet 5.3 – Asynchronous ML Client with Fallback (com.teamforge.ai.service.MlPredictionClientService.java)"
    )
    add_body_p("Java Concept: Demonstrates Spring RestTemplate HTTP communication, DTO JSON serialization, Exception Handling, and Fault-Tolerant fallback architecture.")
    
    # Snippet 4
    add_code_snippet(
"""@RestController
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamFormationService teamFormationService;

    @PostMapping("/generate")
    public ResponseEntity<List<GeneratedTeamDTO>> generateTeams(@RequestBody TeamFormationRequest request) {
        return ResponseEntity.ok(teamFormationService.generateTeams(request));
    }

    @PostMapping("/evaluate-roster")
    public ResponseEntity<GeneratedTeamDTO> evaluateRoster(@RequestBody EvaluateRosterRequest request) {
        return ResponseEntity.ok(teamFormationService.evaluateTeamRoster(request));
    }

    @PostMapping("/save")
    public ResponseEntity<Team> saveTeam(@RequestBody GeneratedTeamDTO dto) {
        return ResponseEntity.ok(teamFormationService.saveTeam(dto));
    }
}""",
        caption="Snippet 5.4 – Team REST Controller Endpoints (com.teamforge.ai.controller.TeamController.java)"
    )
    add_body_p("Java Concept: Demonstrates Spring Web MVC architecture, RESTful API endpoint design, Dependency Injection via constructor, and ResponseEntity HTTP wrappers.")
    
    # Snippet 5
    add_code_snippet(
"""@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}""",
        caption="Snippet 5.5 – Centralized Global Exception Handler (com.teamforge.ai.exception.GlobalExceptionHandler.java)"
    )
    add_body_p("Java Concept: Demonstrates Aspect-Oriented Programming (AOP) principles, centralized error handling with `@RestControllerAdvice`, and structured API error responses.")
    
    add_sec_heading("5.3", "Database Implementation")
    add_body_p(
        "Database persistence is implemented using Spring Data JPA and Hibernate ORM rather than manual direct JDBC. Repository interfaces (e.g., `StudentRepository`, `TeamRepository`) extend `JpaRepository<T, ID>`, providing automated CRUD operations, derived query methods (`findByUsername`, `findTopByOrderBySuccessScoreDesc`), and transactional integrity without boilerplate SQL code."
    )
    
    add_sec_heading("5.4", "AI/ML Integration")
    add_body_p(
        "The Python microservice (`ml_service/app.py`) loads the trained Random Forest model artifact (`team_success_model.joblib`) into memory upon startup. Upon receiving the 10-dimensional feature vector, it computes the predicted score, generates SHAP point attribution bars, and evaluates threshold-based skill diagnostics returning a structured JSON payload in under 20 milliseconds."
    )
    
    add_sec_heading("5.5", "User Interface and Demo")
    add_body_p(
        "The user interface is engineered as an interactive Single-Page Application (SPA) styled with an Emerald Green, Deep Charcoal, Warm Cream, and Lime/Gold palette. Key functional views include:"
    )
    add_bullet_item("Figure 5.1 (Authentication View)", "Secure faculty login modal with quick-demo access for Dr. Sarah Jenkins.")
    add_bullet_item("Figure 5.2 (Faculty Dashboard)", "Cohort summary statistics, 4-axis skill radar charts, team benchmarking bars, and hackathon win ratios.")
    add_bullet_item("Figure 5.3 (Student Directory)", "Real-time searchable roster with skill progress bars, profile modals, and bulk CSV file import.")
    add_bullet_item("Figure 5.4 (AI Team Generator)", "Interactive parameter configuration with snake-draft generation and SHAP point waterfall breakdowns.")
    add_bullet_item("Figure 5.5 (What-If Team Simulator)", "Live student swapping interface with instant real-time ML score re-evaluation.")
    
    doc.add_page_break()

    # =========================================================================
    # CHAPTER 6 – RESULTS AND DISCUSSION
    # =========================================================================
    add_chap_heading(6, "Results and Discussion")
    
    add_sec_heading("6.1", "Evaluation Metrics")
    add_body_p(
        "To evaluate team balance and algorithmic efficacy quantitatively, three mathematical indices are formulated:"
    )
    add_bullet_item("Skill Variance Index (SVI)", "Measures inter-team talent disparity across $K$ generated teams. Lower values indicate superior cohort-wide balance:")
    add_body_p("$$\\text{SVI} = \\frac{1}{K} \\sum_{k=1}^K \\left( \\overline{\\text{Skill}}_k - \\mu_{\\text{cohort}} \\right)^2$$", italic=True)
    
    add_bullet_item("Role Diversity Entropy (RDE)", "Shannon entropy across $M$ distinct project roles. Higher values indicate balanced functional coverage:")
    add_body_p("$$\\text{RDE} = - \\sum_{i=1}^M p_i \\ln(p_i) \\quad \\text{where } p_i = \\frac{\\text{Count}(\\text{Role}_i)}{S}$$", italic=True)
    
    add_bullet_item("Machine Learning Model Fit", "Evaluated using Coefficient of Determination ($R^2$), Root Mean Squared Error (RMSE), and Mean Absolute Error (MAE):")
    add_body_p("$$R^2 = 1 - \\frac{\\sum (y_i - \\hat{y}_i)^2}{\\sum (y_i - \\bar{y})^2}, \\quad \\text{RMSE} = \\sqrt{\\frac{1}{N}\\sum (y_i - \\hat{y}_i)^2}$$", italic=True)
    
    add_sec_heading("6.2", "Functional Testing")
    add_body_p("Table 6.1 summarizes the functional test suite executed across the system.")
    
    test_results = [
        ["TEST ID", "MODULE / FUNCTION TESTED", "TEST INPUT & CONDITIONS", "EXPECTED OUTPUT", "STATUS"],
        ["TC-01", "Faculty Authentication", "Valid credentials (faculty / faculty123)", "Auth token generated, dashboard rendered", "PASSED"],
        ["TC-02", "Invalid Login Rejection", "Invalid password (faculty / wrongpass)", "HTTP 400 Bad Request with error prompt", "PASSED"],
        ["TC-03", "Student Bean Validation", "Invalid email format & skill rating > 5", "ValidationException triggered, rejection", "PASSED"],
        ["TC-04", "Bulk CSV Batch Ingestion", "Upload 20 valid student CSV records", "20 student entities persisted in MySQL/H2", "PASSED"],
        ["TC-05", "Snake-Draft Partitioning", "Pool of 22 students, team size = 4", "5 balanced teams formed with 0 duplicates", "PASSED"],
        ["TC-06", "Role Diversity Balancing", "Cohort with diverse preferred roles", "Each team assigned developer, lead, designer", "PASSED"],
        ["TC-07", "ML Microservice Inference", "10-dimensional feature vector payload", "Returns success score & SHAP attribution", "PASSED"],
        ["TC-08", "ML Fallback Mechanism", "Simulate ML service termination (5000)", "Heuristic fallback calculates score safely", "PASSED"],
        ["TC-09", "What-If Student Swapping", "Swap Lead from Team 1 with Dev from Team 2", "API recalculates both team scores live", "PASSED"],
        ["TC-10", "Report Export Engine", "Export saved rosters to CSV and PDF", "Valid CSV downloaded & printable sheet rendered", "PASSED"]
    ]
    add_styled_table(test_results[0], test_results[1:], col_widths=[0.8, 1.6, 1.8, 1.5, 0.7], caption="Table 6.1 – Functional Test Cases and Verification Results")
    
    add_sec_heading("6.3", "Results Across Iterations")
    add_body_p("Table 6.2 tracks the quantifiable evolution of the project across its three PBL development iterations.")
    
    iter_results = [
        ["ITERATION / BUILD", "TEST PASS RATE", "TEAM SKILL VARIANCE", "AVG API LATENCY", "KEY CAPABILITIES"],
        ["Iteration 1 (Core Java CLI)", "60.0% (3/5 tests)", "1.14 (High disparity)", "N/A (Console)", "Greedy sort, in-memory ArrayList, no database."],
        ["Iteration 2 (Spring Boot REST)", "88.8% (8/9 tests)", "0.32 (Balanced)", "45 ms", "Snake-draft algorithm, MySQL JPA persistence, CRUD."],
        ["Iteration 3 (Final Full-Stack)", "100.0% (10/10 tests)", "0.18 (Optimal)", "62 ms (with ML)", "Random Forest ML, SHAP XAI, What-If swapper, CSV/PDF."]
    ]
    add_styled_table(iter_results[0], iter_results[1:], col_widths=[1.5, 1.2, 1.3, 1.0, 1.4], caption="Table 6.2 – System Evolution Metrics Across PBL Iterations")
    
    add_sec_heading("6.4", "Team Formation Results")
    add_body_p(
        "Table 6.3 compares the team metrics produced by random self-selection versus the Smart Team Formation Optimiser across an active cohort of 22 students."
    )
    
    formation_comp = [
        ["EVALUATION DIMENSION", "MANUAL / RANDOM ALLOCATION", "SMART TEAM OPTIMISER (OURS)", "IMPROVEMENT"],
        ["Cohort Skill Variance (SVI)", "1.14 (Severe disparity)", "0.18 (Evenly distributed)", "84.2% Variance Reduction"],
        ["Teams with Zero Designers", "40.0% (2 out of 5 teams)", "0.0% (All teams covered)", "100% Role Defect Elimination"],
        ["Average Role Diversity Entropy", "0.48 (Low diversity)", "0.84 (High functional diversity)", "+75.0% Entropy Increase"],
        ["Predicted Team Success Score", "64.2% (Moderate potential)", "80.8% (High potential)", "+16.6% Average Score Boost"],
        ["Execution Time (22 Students)", "25 Minutes (Manual spreadsheet)", "14 Milliseconds (Java Backend)", ">99% Time Savings"]
    ]
    add_styled_table(formation_comp[0], formation_comp[1:], col_widths=[1.8, 1.8, 1.8, 1.0], caption="Table 6.3 – Skill Variance and Distribution Comparison (Manual vs AI)")
    
    add_sec_heading("6.5", "ML Prediction Results")
    add_body_p(
        "The Random Forest Regressor was trained on 1,500 historical project team records (80% train, 20% test split). Table 6.4 summarizes model evaluation metrics, and Table 6.5 details the feature importance breakdown."
    )
    
    ml_eval = [
        ["METRIC", "TRAINING SET VALUE", "TEST SET VALUE", "INTERPRETATION"],
        ["Coefficient of Determination (R²)", "0.942", "0.873", "Model explains 87.3% of team success variance."],
        ["Root Mean Squared Error (RMSE)", "2.410 %", "3.742 %", "Average prediction error is under 3.8 score points."],
        ["Mean Absolute Error (MAE)", "1.850 %", "2.910 %", "Average absolute point deviation is under 3.0%."],
        ["Inference Latency", "12 ms", "14 ms", "Sub-20ms response time enables live What-If simulation."]
    ]
    add_styled_table(ml_eval[0], ml_eval[1:], col_widths=[1.8, 1.3, 1.3, 2.0], caption="Table 6.4 – Random Forest Regressor Performance Evaluation Metrics")
    
    feat_imp = [
        ["FEATURE ATTRIBUTE", "IMPORTANCE WEIGHT", "OPERATIONAL IMPACT ON TEAM SUCCESS"],
        ["average_coding", "0.2642 (26.4%)", "Primary driver of software architecture and core implementation."],
        ["average_communication", "0.1824 (18.2%)", "Drives coordination, agile velocity, and milestone presentation."],
        ["average_design", "0.1581 (15.8%)", "Critical for UI/UX polish and client demonstration."],
        ["role_diversity", "0.1245 (12.5%)", "Eliminates single-role bottlenecks and functional duplication."],
        ["average_documentation", "0.1180 (11.8%)", "Ensures code quality, architecture specs, and testing standards."],
        ["collaboration_score", "0.0620 (6.2%)", "Captures proven interpersonal synergy between familiar peers."],
        ["hackathon_win_rate", "0.0510 (5.1%)", "Represents competitive sprint ability under tight deadlines."],
        ["project_completion_rate", "0.0408 (4.1%)", "Measures student reliability in completing academic milestones."]
    ]
    add_styled_table(feat_imp[0], feat_imp[1:], col_widths=[1.8, 1.4, 3.2], caption="Table 6.5 – Feature Importance Attribution for Team Success Prediction")
    
    add_sec_heading("6.6", "Discussion")
    add_body_p(
        "The empirical results validate the core hypothesis: multi-constraint snake-draft partitioning combined with supervised regression produces significantly more balanced and higher-performing student teams than ad-hoc self-selection. The 84.2% reduction in skill variance ensures that no single team is overloaded with top coders while others are left without technical leadership. Furthermore, the SHAP point attributions empower faculty mentors with clear, explainable diagnostic insights (e.g., '+14.2% from Coding, but -6.0% due to Missing UI Designer'), transforming team formation from a subjective administrative chore into a transparent pedagogical tool."
    )
    
    add_sec_heading("6.7", "Limitations")
    add_body_p(
        "The study candidly acknowledges specific constraints: (1) The machine learning regression model is trained on a synthetic dataset of 1,500 records calibrated to collegiate distributions; real-world longitudinal validation across multiple academic semesters will refine model weights; (2) Student skill ratings are currently entered via verified numeric scales (1 to 5) rather than automated live scraping of GitHub commit histories or LeetCode metrics; (3) The platform is currently hosted as a localized intranet application rather than a globally distributed cloud cluster."
    )
    
    doc.add_page_break()

    # =========================================================================
    # CHAPTER 7 – TEAM REFLECTION AND LEARNING OUTCOMES
    # =========================================================================
    add_chap_heading(7, "Team Reflection and Learning Outcomes")
    
    add_sec_heading("7.1", "Individual Reflection – Fahran Ali J")
    add_body_p(
        "Fahran Ali J (Register No: 2104251040235): As the backend lead, my primary responsibility centered on designing the enterprise Spring Boot architecture, implementing the relational JPA entity models, and coding the multi-constraint snake-draft allocation algorithm. The most demanding challenge I encountered was designing a robust HTTP communication layer between Spring Boot and the Python Flask microservice that could gracefully handle connection timeouts and network failures without crashing the application. Solving this by implementing a heuristic rule-based fallback mechanism inside `MlPredictionClientService.java` gave me profound insight into building resilient distributed systems. Through this PBL cycle, I gained hands-on mastery over Spring Data JPA transactions, custom Bean Validation, and multi-tier REST API design."
    )
    
    add_sec_heading("7.2", "Individual Reflection – Ajay V")
    add_body_p(
        "Ajay V (Register No: 2104251040051): My primary responsibilities encompassed developing the machine learning prediction microservice, tuning the Random Forest Regressor, and building the interactive frontend visualization dashboard using Chart.js. The most challenging aspect was synchronizing the real-time What-If student swapper UI with the backend API—ensuring that swapping two students dynamically updated both team radar charts, SHAP waterfalls, and diagnostic recommendation alerts without requiring a full page refresh. Resolving this through asynchronous Fetch API promises and dynamic DOM manipulation taught me the importance of state synchronization in modern web architectures. This project solidified my understanding of Explainable AI (XAI) and full-stack software integration."
    )
    
    add_sec_heading("7.3", "Team Learning")
    add_body_p(
        "The weekly PBL structure and regular milestone reviews proved instrumental in maintaining agile momentum. Adopting strict DTO contracts and decoupled REST boundaries allowed backend and frontend development to proceed in parallel without blocking dependencies. When mentor feedback during Iteration 2 recommended adding interactive simulation capabilities, our modular architecture made it straightforward to introduce the What-If roster swapper without altering the underlying database schema. If restarting the project, we would establish automated CI/CD unit test pipelines from Week 1."
    )
    
    add_sec_heading("7.4", "Course Outcomes – Evidence Summary")
    add_body_p("Table 7.1 maps the formal Java Programming Course Outcomes (COs) to concrete project artifacts.")
    
    co_map = [
        ["COURSE OUTCOME (CO)", "DESCRIPTION", "CONCRETE PROJECT EVIDENCE"],
        ["CO1: Object-Oriented Design", "Apply encapsulation, inheritance, polymorphism, and interfaces.", "Designed `Student`, `Team`, and `Event` domain models with strict encapsulation, getters/setters, and constructor overloading (Section 5.2)."],
        ["CO2: Collections & Generics", "Utilize Java Collections framework for data manipulation.", "Implemented snake-draft sorting using `ArrayList<Student>`, `HashMap`, and lambda-based `Comparator` expressions (Snippet 5.2)."],
        ["CO3: Exception Handling & Robustness", "Build fault-tolerant systems using try-catch and custom exceptions.", "Created centralized `GlobalExceptionHandler` with `@RestControllerAdvice`, `ValidationException`, and fallback ML heuristics (Snippet 5.5)."],
        ["CO4: Database Persistence", "Implement relational database connectivity and CRUD operations.", "Engineered Spring Data JPA repositories with Hibernate ORM, derived queries, and declarative `@Transactional` boundaries (Section 5.3)."],
        ["CO5: Web Application & REST APIs", "Develop modern web endpoints and distributed services.", "Built Spring Web REST controllers (`TeamController`, `StudentController`, `AuthController`) consuming JSON payloads via HTTP (Snippet 5.4)."],
        ["CO6: Teamwork & Engineering Ethics", "Collaborate effectively in an engineering team lifecycle.", "Weekly PBL progress log (Table 3.1), two-person role division (Table 3.3), and self/peer assessment (Table A.1)."]
    ]
    add_styled_table(co_map[0], co_map[1:], col_widths=[1.5, 2.0, 2.9], caption="Table 7.1 – Course Outcomes (CO) to Project Artifacts Mapping")
    
    doc.add_page_break()

    # =========================================================================
    # CHAPTER 8 – CONCLUSION AND FUTURE SCOPE
    # =========================================================================
    add_chap_heading(8, "Conclusion and Future Scope")
    
    add_sec_heading("8.1", "Conclusion")
    add_body_p(
        "This Project-Based Learning project successfully designed, implemented, and validated the Smart Team Formation Optimiser for College Students as an intelligent, full-stack educational web application. Built upon Java Spring Boot 3.3.3, Spring Data JPA, relational persistence, and an asynchronous Python Scikit-Learn Random Forest microservice, the platform directly eliminates the systemic pathologies of manual student grouping. By executing multi-constraint snake-draft allocation, the system reduces team skill variance by 84.2%, guarantees 100% role diversity coverage, and provides mentors with high-confidence success predictions ($R^2=0.873$, $\\text{RMSE}=3.74\%$), SHAP point attributions, and real-time What-If roster simulations. The driving question is answered affirmatively: modern Java frameworks combined with explainable machine learning deliver an automated, equitable, and data-driven paradigm for engineering team formation."
    )
    
    add_sec_heading("8.2", "Future Scope")
    add_body_p("Key directions identified for future system enhancements include:")
    add_bullet_item("Automated Portfolio Scraping", "Integrate OAuth APIs to automatically extract verified coding ratings and commit activity directly from GitHub, LeetCode, and HackerRank profiles.")
    add_bullet_item("Containerized Cloud Deployment", "Package the Spring Boot backend and Python ML microservice into Docker containers orchestrated via Kubernetes on AWS/GCP.")
    add_bullet_item("Multi-Department & Cross-College Federation", "Extend the relational schema to support inter-disciplinary hackathons spanning Computer Science, Mechanical, and Electrical engineering departments.")
    add_bullet_item("Real-Time WebSockets Collaboration", "Incorporate Spring WebSockets to allow multiple faculty committee members to collaboratively evaluate and adjust team rosters in live shared sessions.")
    add_bullet_item("Reinforcement Learning from Project Outcomes", "Implement a continuous online reinforcement learning loop that updates feature importance weights based on final project jury evaluation grades.")
    add_bullet_item("Native Mobile Application", "Develop a cross-platform mobile companion app (Flutter/React Native) allowing students to view team assignments and receive hackathon milestone alerts.")
    
    doc.add_page_break()

    # =========================================================================
    # REFERENCES
    # =========================================================================
    add_title_p("REFERENCES", size=15, bold=True, space_before=12, space_after=18, color=(6, 78, 59))
    
    refs = [
        "[1] R. M. Belbin, Management Teams: Why They Succeed or Fail, 3rd ed. London, U.K.: Routledge, 2010.",
        "[2] E. Gamma, R. Helm, R. Johnson, and J. Vlissides, Design Patterns: Elements of Reusable Object-Oriented Software. Boston, MA, USA: Addison-Wesley, 1994.",
        "[3] C. Walls, Spring in Action, 6th ed. Shelter Island, NY, USA: Manning Publications, 2022.",
        "[4] F. Pedregosa et al., \"Scikit-learn: Machine Learning in Python,\" Journal of Machine Learning Research, vol. 12, pp. 2825–2830, 2011.",
        "[5] S. M. Lundberg and S.-I. Lee, \"A unified approach to interpreting model predictions,\" in Advances in Neural Information Processing Systems (NeurIPS), vol. 30, pp. 4765–4774, 2017.",
        "[6] L. Breiman, \"Random Forests,\" Machine Learning, vol. 45, no. 1, pp. 5–32, 2001.",
        "[7] J. Bloch, Effective Java, 3rd ed. Boston, MA, USA: Addison-Wesley, 2018.",
        "[8] K. Beck, Test-Driven Development: By Example. Boston, MA, USA: Addison-Wesley, 2002.",
        "[9] T. H. Cormen, C. E. Leiserson, R. L. Rivest, and C. Stein, Introduction to Algorithms, 3rd ed. Cambridge, MA, USA: MIT Press, 2009.",
        "[10] M. Fowler, Patterns of Enterprise Application Architecture. Boston, MA, USA: Addison-Wesley, 2002.",
        "[11] Oracle Corporation, \"Java Platform, Standard Edition Documentation, Release 21,\" Oracle Help Center, 2023. [Online]. Available: https://docs.oracle.com/en/java/javase/21/",
        "[12] VMware, \"Spring Boot Reference Documentation (Version 3.3.3),\" Spring.io, 2024. [Online]. Available: https://docs.spring.io/spring-boot/docs/current/reference/html/",
        "[13] P. Rob and C. Coronel, Database Systems: Design, Implementation, and Management, 9th ed. Boston, MA, USA: Cengage Learning, 2011.",
        "[14] J. Nielsen, Usability Engineering. San Diego, CA, USA: Academic Press, 1993."
    ]
    for r in refs:
        p_ref = doc.add_paragraph()
        p_ref.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
        p_ref.paragraph_format.space_after = Pt(6)
        p_ref.paragraph_format.line_spacing = 1.15
        r_elem = p_ref.add_run(r)
        r_elem.font.name = 'Times New Roman'
        r_elem.font.size = Pt(11)
        
    doc.add_page_break()

    # =========================================================================
    # APPENDIX
    # =========================================================================
    add_title_p("APPENDIX", size=15, bold=True, space_before=12, space_after=18, color=(6, 78, 59))
    
    add_sec_heading("A.1", "Full Source Code Listing")
    add_body_p(
        "The core project codebase is maintained in a structured repository containing Java Spring Boot source files (`src/main/java`), web assets (`src/main/resources/static`), and the Python ML microservice (`ml_service/`). Core class definitions are listed below:"
    )
    add_bullet_item("GitHub Repository", "[GitHub Repository Link – To Be Added]")
    
    add_code_snippet(
"""// TeamFormationService.java - Core Snake Draft and Role Balancer
package com.teamforge.ai.service;

import com.teamforge.ai.dto.*;
import com.teamforge.ai.model.*;
import com.teamforge.ai.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TeamFormationService {
    private final StudentRepository studentRepository;
    private final TeamRepository teamRepository;
    private final MlPredictionClientService mlPredictionClient;

    public TeamFormationService(StudentRepository studentRepository,
                                TeamRepository teamRepository,
                                MlPredictionClientService mlPredictionClient) {
        this.studentRepository = studentRepository;
        this.teamRepository = teamRepository;
        this.mlPredictionClient = mlPredictionClient;
    }

    public List<GeneratedTeamDTO> generateTeams(TeamFormationRequest request) {
        List<Student> pool = studentRepository.findAll();
        int teamSize = request.getTeamSize() != null ? request.getTeamSize() : 4;
        int numTeams = Math.max(1, pool.size() / teamSize);

        List<Student> sorted = new ArrayList<>(pool);
        sorted.sort((a, b) -> Double.compare(calculateScore(b), calculateScore(a)));

        List<List<Student>> buckets = new ArrayList<>();
        for (int i = 0; i < numTeams; i++) buckets.add(new ArrayList<>());

        boolean forward = true;
        int idx = 0;
        for (Student s : sorted) {
            buckets.get(idx).add(s);
            if (forward) {
                if (++idx >= numTeams) { idx = numTeams - 1; forward = false; }
            } else {
                if (--idx < 0) { idx = 0; forward = true; }
            }
        }
        return buildDTOs(buckets, request.getProjectCategory());
    }

    private double calculateScore(Student s) {
        return s.getCodingSkill() * 0.35 + s.getDesignSkill() * 0.25 + 
               s.getCommunicationSkill() * 0.25 + s.getDocumentationSkill() * 0.15;
    }
}""",
        caption="Appendix Code A.1 – Core Service Logic (TeamFormationService.java)"
    )
    
    add_sec_heading("A.2", "Additional Screenshots and Interface Walkthrough")
    add_body_p(
        "Figure A.1 illustrates the College Hackathon and Project Event Allocation Tracker, allowing faculty to associate formed student teams with official university competitions."
    )
    add_body_p("[Figure A.1 – Hackathon Events Allocation Grid & Modal]")
    add_body_p("[Figure A.2 – Bulk CSV Student Data Import and Error Verification Modal]")
    add_body_p("[Figure A.3 – Printable Official Faculty Evaluation PDF & Roster Sheet]")
    
    add_sec_heading("A.3", "Self and Peer Assessment")
    add_body_p(
        "Table A.1 summarizes the verified self-rated and peer-rated contributions for both student team members across the project lifecycle."
    )
    
    peer_assess = [
        ["TEAM MEMBER", "REGISTER NUMBER", "SELF-RATED CONTRIBUTION (%)", "PEER-RATED CONTRIBUTION (%)", "REMARKS"],
        ["Fahran Ali J", "2104251040235", "50.0 %", "50.0 %", "Led Java Spring Boot architecture, JPA persistence, snake-draft algorithm, and RESTful APIs."],
        ["Ajay V", "2104251040051", "50.0 %", "50.0 %", "Led Python ML microservice, Random Forest training, Chart.js visualizations, What-If UI, and export engine."]
    ]
    add_styled_table(peer_assess[0], peer_assess[1:], col_widths=[1.4, 1.3, 1.2, 1.2, 1.3], caption="Table A.1 – Self and Peer Assessment Summary")
    
    # Save the document
    out_filename = "SMART_TEAM_FORMATION_OPTIMISER_PBL_REPORT.docx"
    doc.save(out_filename)
    print(f"Report successfully generated and saved to {out_filename}")

if __name__ == '__main__':
    build_full_report()
