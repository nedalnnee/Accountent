#!/usr/bin/env python3
"""
MGM Resorts 2023 Cyberattack - Case Analysis Presentation Generator
Creates a professional PowerPoint presentation analyzing the MGM Resorts cyberattack.
Student: Amr Omair
"""

from pptx import Presentation
from pptx.util import Inches, Pt, Emu
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN, MSO_ANCHOR
from pptx.enum.shapes import MSO_SHAPE
import os

DARK_BG = RGBColor(0x1A, 0x1A, 0x2E)
ACCENT_BLUE = RGBColor(0x00, 0x7B, 0xFF)
ACCENT_RED = RGBColor(0xE8, 0x3E, 0x3E)
ACCENT_GOLD = RGBColor(0xFF, 0xB8, 0x00)
ACCENT_GREEN = RGBColor(0x00, 0xC9, 0x7B)
WHITE = RGBColor(0xFF, 0xFF, 0xFF)
LIGHT_GRAY = RGBColor(0xCC, 0xCC, 0xCC)
MEDIUM_GRAY = RGBColor(0x99, 0x99, 0x99)
DARK_TEXT = RGBColor(0x22, 0x22, 0x22)
SLIDE_BG_DARK = RGBColor(0x0F, 0x0F, 0x23)
CARD_BG = RGBColor(0x1E, 0x1E, 0x3A)

SLIDE_WIDTH = Inches(13.333)
SLIDE_HEIGHT = Inches(7.5)


def set_slide_bg(slide, color):
    bg = slide.background
    fill = bg.fill
    fill.solid()
    fill.fore_color.rgb = color


def add_shape_with_fill(slide, left, top, width, height, color, corner_radius=None):
    shape = slide.shapes.add_shape(
        MSO_SHAPE.ROUNDED_RECTANGLE if corner_radius else MSO_SHAPE.RECTANGLE,
        left, top, width, height
    )
    shape.fill.solid()
    shape.fill.fore_color.rgb = color
    shape.line.fill.background()
    if corner_radius:
        shape.adjustments[0] = corner_radius
    return shape


def add_text_box(slide, left, top, width, height, text, font_size=18,
                 color=WHITE, bold=False, alignment=PP_ALIGN.LEFT,
                 font_name="Calibri", line_spacing=1.2):
    txBox = slide.shapes.add_textbox(left, top, width, height)
    tf = txBox.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = text
    p.font.size = Pt(font_size)
    p.font.color.rgb = color
    p.font.bold = bold
    p.font.name = font_name
    p.alignment = alignment
    p.space_after = Pt(0)
    p.space_before = Pt(0)
    if line_spacing != 1.0:
        p.line_spacing = Pt(font_size * line_spacing)
    return txBox


def add_multiline_text(slide, left, top, width, height, lines, font_size=16,
                       color=WHITE, font_name="Calibri", alignment=PP_ALIGN.LEFT,
                       line_spacing=1.5, bold=False):
    txBox = slide.shapes.add_textbox(left, top, width, height)
    tf = txBox.text_frame
    tf.word_wrap = True

    for i, line in enumerate(lines):
        if i == 0:
            p = tf.paragraphs[0]
        else:
            p = tf.add_paragraph()

        if isinstance(line, tuple):
            p.text = line[0]
            p.font.size = Pt(line[1] if len(line) > 1 else font_size)
            p.font.color.rgb = line[2] if len(line) > 2 else color
            p.font.bold = line[3] if len(line) > 3 else bold
        else:
            p.text = line
            p.font.size = Pt(font_size)
            p.font.color.rgb = color
            p.font.bold = bold

        p.font.name = font_name
        p.alignment = alignment
        p.space_after = Pt(4)
        p.line_spacing = Pt(font_size * line_spacing)

    return txBox


def add_accent_line(slide, left, top, width, color=ACCENT_BLUE):
    shape = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, left, top, width, Pt(4))
    shape.fill.solid()
    shape.fill.fore_color.rgb = color
    shape.line.fill.background()
    return shape


def add_bullet_list(slide, left, top, width, height, items, font_size=15,
                    color=LIGHT_GRAY, bullet_color=ACCENT_BLUE, font_name="Calibri",
                    line_spacing=1.6):
    txBox = slide.shapes.add_textbox(left, top, width, height)
    tf = txBox.text_frame
    tf.word_wrap = True

    for i, item in enumerate(items):
        if i == 0:
            p = tf.paragraphs[0]
        else:
            p = tf.add_paragraph()

        if isinstance(item, tuple):
            p.text = f"\u2022  {item[0]}"
            p.font.size = Pt(item[1] if len(item) > 1 else font_size)
            p.font.color.rgb = item[2] if len(item) > 2 else color
        else:
            p.text = f"\u2022  {item}"
            p.font.size = Pt(font_size)
            p.font.color.rgb = color

        p.font.name = font_name
        p.alignment = PP_ALIGN.LEFT
        p.space_after = Pt(6)
        p.line_spacing = Pt(font_size * line_spacing)

    return txBox


def add_slide_number(slide, num, total):
    add_text_box(
        slide, Inches(12.2), Inches(7.0), Inches(1), Inches(0.4),
        f"{num} / {total}", font_size=10, color=MEDIUM_GRAY,
        alignment=PP_ALIGN.RIGHT
    )


def create_presentation():
    prs = Presentation()
    prs.slide_width = SLIDE_WIDTH
    prs.slide_height = SLIDE_HEIGHT

    total_slides = 12
    slide_layout = prs.slide_layouts[6]  # blank layout

    # =========================================================================
    # SLIDE 1: Title Slide
    # =========================================================================
    slide = prs.slides.add_slide(slide_layout)
    set_slide_bg(slide, SLIDE_BG_DARK)

    add_accent_line(slide, Inches(0), Inches(0), SLIDE_WIDTH, ACCENT_RED)

    add_shape_with_fill(slide, Inches(0.8), Inches(1.5), Inches(0.15), Inches(1.2), ACCENT_RED)

    add_text_box(
        slide, Inches(1.3), Inches(1.5), Inches(10), Inches(0.8),
        "CASE ANALYSIS PRESENTATION", font_size=16, color=ACCENT_RED,
        bold=True, font_name="Calibri"
    )

    add_text_box(
        slide, Inches(1.3), Inches(2.1), Inches(10), Inches(1.2),
        "MGM Resorts International", font_size=44, color=WHITE,
        bold=True, font_name="Calibri"
    )
    add_text_box(
        slide, Inches(1.3), Inches(3.2), Inches(10), Inches(0.8),
        "Cyberattack  |  September 2023", font_size=28, color=ACCENT_GOLD,
        bold=False, font_name="Calibri"
    )

    add_accent_line(slide, Inches(1.3), Inches(4.2), Inches(3), ACCENT_RED)

    add_text_box(
        slide, Inches(1.3), Inches(4.6), Inches(5), Inches(0.5),
        "Security  /  Human Factors  /  Business Continuity", font_size=14,
        color=LIGHT_GRAY, font_name="Calibri"
    )

    add_text_box(
        slide, Inches(1.3), Inches(5.5), Inches(5), Inches(0.4),
        "Prepared by:  Amr Omair", font_size=18, color=WHITE,
        bold=False, font_name="Calibri"
    )
    add_text_box(
        slide, Inches(1.3), Inches(6.0), Inches(5), Inches(0.4),
        "Assignment 3  \u2014  Case Analysis", font_size=13, color=MEDIUM_GRAY,
        font_name="Calibri"
    )

    info_card = add_shape_with_fill(
        slide, Inches(8.5), Inches(2.0), Inches(4), Inches(3.5),
        CARD_BG, corner_radius=0.05
    )
    add_multiline_text(
        slide, Inches(8.8), Inches(2.2), Inches(3.5), Inches(3.2),
        [
            ("KEY FACTS", 13, ACCENT_RED, True),
            ("", 8, CARD_BG),
            ("\u2022  10-day operational shutdown", 13, LIGHT_GRAY),
            ("\u2022  $100 million in losses", 13, LIGHT_GRAY),
            ("\u2022  6 TB of data exfiltrated", 13, LIGHT_GRAY),
            ("\u2022  100+ servers encrypted", 13, LIGHT_GRAY),
            ("\u2022  29 properties affected", 13, LIGHT_GRAY),
            ("\u2022  $45M class-action settlement", 13, LIGHT_GRAY),
        ],
        line_spacing=1.6
    )

    add_slide_number(slide, 1, total_slides)

    # =========================================================================
    # SLIDE 2: Table of Contents
    # =========================================================================
    slide = prs.slides.add_slide(slide_layout)
    set_slide_bg(slide, SLIDE_BG_DARK)
    add_accent_line(slide, Inches(0), Inches(0), SLIDE_WIDTH, ACCENT_BLUE)

    add_text_box(
        slide, Inches(0.8), Inches(0.5), Inches(5), Inches(0.7),
        "PRESENTATION OVERVIEW", font_size=14, color=ACCENT_BLUE, bold=True
    )
    add_text_box(
        slide, Inches(0.8), Inches(1.0), Inches(10), Inches(0.7),
        "Agenda", font_size=36, color=WHITE, bold=True
    )
    add_accent_line(slide, Inches(0.8), Inches(1.8), Inches(2.5), ACCENT_BLUE)

    sections = [
        ("01", "Background & Summary", "Overview of MGM Resorts and the incident"),
        ("02", "Key Parties Involved", "Threat actors, victims, and responders"),
        ("03", "What Happened", "Detailed attack timeline and methodology"),
        ("04", "The Attack Flow", "Technical breakdown of the cyberattack"),
        ("05", "Why It Became Important", "Scale and significance of the breach"),
        ("06", "Cybersecurity Issues", "Security, privacy, and ethical concerns"),
        ("07", "Violated Rules & Laws", "Legal and regulatory frameworks breached"),
        ("08", "Consequences & Impact", "Financial, operational, and reputational damage"),
        ("09", "Personal Opinion", "Critical analysis and perspective"),
        ("10", "Lessons Learned", "Recommendations and takeaways"),
    ]

    col1_x = Inches(0.8)
    col2_x = Inches(6.5)
    start_y = Inches(2.3)
    row_h = Inches(0.48)

    for i, (num, title, desc) in enumerate(sections):
        col = col1_x if i < 5 else col2_x
        y = start_y + (i % 5) * row_h

        add_text_box(slide, col, y, Inches(0.5), Inches(0.4),
                     num, font_size=14, color=ACCENT_BLUE, bold=True)
        add_text_box(slide, col + Inches(0.5), y, Inches(2.5), Inches(0.4),
                     title, font_size=14, color=WHITE, bold=True)
        add_text_box(slide, col + Inches(0.5), y + Inches(0.22), Inches(4.5), Inches(0.3),
                     desc, font_size=10, color=MEDIUM_GRAY)

    add_slide_number(slide, 2, total_slides)

    # =========================================================================
    # SLIDE 3: Background & Summary
    # =========================================================================
    slide = prs.slides.add_slide(slide_layout)
    set_slide_bg(slide, SLIDE_BG_DARK)
    add_accent_line(slide, Inches(0), Inches(0), SLIDE_WIDTH, ACCENT_BLUE)

    add_text_box(slide, Inches(0.8), Inches(0.5), Inches(3), Inches(0.5),
                 "01  BACKGROUND", font_size=14, color=ACCENT_BLUE, bold=True)
    add_text_box(slide, Inches(0.8), Inches(1.0), Inches(10), Inches(0.7),
                 "Background & Summary of the Incident", font_size=32, color=WHITE, bold=True)
    add_accent_line(slide, Inches(0.8), Inches(1.8), Inches(2.5), ACCENT_BLUE)

    # About MGM Card
    card1 = add_shape_with_fill(slide, Inches(0.8), Inches(2.3), Inches(5.5), Inches(4.5),
                                 CARD_BG, corner_radius=0.04)
    add_text_box(slide, Inches(1.1), Inches(2.5), Inches(5), Inches(0.4),
                 "ABOUT MGM RESORTS INTERNATIONAL", font_size=12, color=ACCENT_BLUE, bold=True)
    add_accent_line(slide, Inches(1.1), Inches(2.9), Inches(1.5), ACCENT_BLUE)

    add_bullet_list(slide, Inches(1.1), Inches(3.1), Inches(4.8), Inches(3.5),
        [
            "Global hospitality and entertainment company",
            "Portfolio of 29 hotel and resort properties worldwide",
            "Iconic brands: Bellagio, MGM Grand, Mandalay Bay, Aria",
            "Operates 11 gaming and non-gaming resorts in Las Vegas",
            "One of the world's largest gambling and hospitality firms",
            "Publicly traded on NYSE (ticker: MGM)",
        ], font_size=13, line_spacing=1.7)

    # Incident Summary Card
    card2 = add_shape_with_fill(slide, Inches(6.8), Inches(2.3), Inches(5.8), Inches(4.5),
                                 CARD_BG, corner_radius=0.04)
    add_text_box(slide, Inches(7.1), Inches(2.5), Inches(5), Inches(0.4),
                 "THE INCIDENT  \u2014  SEPTEMBER 2023", font_size=12, color=ACCENT_RED, bold=True)
    add_accent_line(slide, Inches(7.1), Inches(2.9), Inches(1.5), ACCENT_RED)

    add_multiline_text(slide, Inches(7.1), Inches(3.1), Inches(5.2), Inches(3.5),
        [
            ("On September 11, 2023, MGM Resorts publicly disclosed that a "
             "\"cybersecurity incident\" was impacting its systems.", 13, LIGHT_GRAY),
            ("", 6, CARD_BG),
            ("The attack was orchestrated by two cybercriminal groups: "
             "Scattered Spider and ALPHV/BlackCat. Using social engineering, "
             "they infiltrated MGM's network, exfiltrated 6 TB of customer data, "
             "and deployed ransomware that encrypted 100+ ESXi servers.", 13, LIGHT_GRAY),
            ("", 6, CARD_BG),
            ("The result was a 10-day operational shutdown across all 29 properties, "
             "costing an estimated $100 million in losses.", 13, WHITE, True),
        ],
        line_spacing=1.5)

    add_slide_number(slide, 3, total_slides)

    # =========================================================================
    # SLIDE 4: Key Parties Involved
    # =========================================================================
    slide = prs.slides.add_slide(slide_layout)
    set_slide_bg(slide, SLIDE_BG_DARK)
    add_accent_line(slide, Inches(0), Inches(0), SLIDE_WIDTH, ACCENT_BLUE)

    add_text_box(slide, Inches(0.8), Inches(0.5), Inches(3), Inches(0.5),
                 "02  KEY PARTIES", font_size=14, color=ACCENT_BLUE, bold=True)
    add_text_box(slide, Inches(0.8), Inches(1.0), Inches(10), Inches(0.7),
                 "Main People, Organizations & Parties Involved", font_size=32, color=WHITE, bold=True)
    add_accent_line(slide, Inches(0.8), Inches(1.8), Inches(2.5), ACCENT_BLUE)

    # Attackers Card
    card = add_shape_with_fill(slide, Inches(0.8), Inches(2.3), Inches(3.7), Inches(4.8),
                                CARD_BG, corner_radius=0.04)
    add_text_box(slide, Inches(1.1), Inches(2.5), Inches(3.2), Inches(0.4),
                 "THREAT ACTORS", font_size=12, color=ACCENT_RED, bold=True)
    add_accent_line(slide, Inches(1.1), Inches(2.9), Inches(1.2), ACCENT_RED)

    add_multiline_text(slide, Inches(1.1), Inches(3.1), Inches(3.2), Inches(3.8),
        [
            ("Scattered Spider", 14, ACCENT_GOLD, True),
            ("(UNC3944 / Storm-0875 / 0ktapus)", 10, MEDIUM_GRAY),
            ("\u2022 English-speaking threat group", 11, LIGHT_GRAY),
            ("\u2022 Specializes in social engineering", 11, LIGHT_GRAY),
            ("\u2022 Members believed to be in their early 20s", 11, LIGHT_GRAY),
            ("\u2022 Active since 2022", 11, LIGHT_GRAY),
            ("", 6, CARD_BG),
            ("ALPHV / BlackCat", 14, ACCENT_GOLD, True),
            ("\u2022 Russia-based RaaS operation", 11, LIGHT_GRAY),
            ("\u2022 Provided ransomware payload", 11, LIGHT_GRAY),
            ("\u2022 Exfiltrated and threatened to leak data", 11, LIGHT_GRAY),
        ],
        line_spacing=1.4)

    # Victim Card
    card = add_shape_with_fill(slide, Inches(4.8), Inches(2.3), Inches(3.7), Inches(4.8),
                                CARD_BG, corner_radius=0.04)
    add_text_box(slide, Inches(5.1), Inches(2.5), Inches(3.2), Inches(0.4),
                 "VICTIM ORGANIZATION", font_size=12, color=ACCENT_BLUE, bold=True)
    add_accent_line(slide, Inches(5.1), Inches(2.9), Inches(1.2), ACCENT_BLUE)

    add_multiline_text(slide, Inches(5.1), Inches(3.1), Inches(3.2), Inches(3.8),
        [
            ("MGM Resorts International", 14, WHITE, True),
            ("\u2022 29 hotel & resort properties", 11, LIGHT_GRAY),
            ("\u2022 Bellagio, MGM Grand, Mandalay Bay", 11, LIGHT_GRAY),
            ("\u2022 Thousands of employees", 11, LIGHT_GRAY),
            ("\u2022 Millions of guest records at risk", 11, LIGHT_GRAY),
            ("", 6, CARD_BG),
            ("Affected Stakeholders", 14, WHITE, True),
            ("\u2022 Hotel guests and casino patrons", 11, LIGHT_GRAY),
            ("\u2022 MGM Rewards loyalty members", 11, LIGHT_GRAY),
            ("\u2022 Pre-2019 customers (PII exposed)", 11, LIGHT_GRAY),
        ],
        line_spacing=1.4)

    # Responders Card
    card = add_shape_with_fill(slide, Inches(8.8), Inches(2.3), Inches(3.7), Inches(4.8),
                                CARD_BG, corner_radius=0.04)
    add_text_box(slide, Inches(9.1), Inches(2.5), Inches(3.2), Inches(0.4),
                 "RESPONDERS & REGULATORS", font_size=12, color=ACCENT_GREEN, bold=True)
    add_accent_line(slide, Inches(9.1), Inches(2.9), Inches(1.2), ACCENT_GREEN)

    add_multiline_text(slide, Inches(9.1), Inches(3.1), Inches(3.2), Inches(3.8),
        [
            ("Law Enforcement", 14, WHITE, True),
            ("\u2022 FBI \u2014 active investigation", 11, LIGHT_GRAY),
            ("\u2022 UK National Crime Agency", 11, LIGHT_GRAY),
            ("\u2022 Arrested 17-year-old suspect (UK)", 11, LIGHT_GRAY),
            ("", 6, CARD_BG),
            ("Cybersecurity Firms", 14, WHITE, True),
            ("\u2022 CrowdStrike & Mandiant", 11, LIGHT_GRAY),
            ("\u2022 Assisted in incident response", 11, LIGHT_GRAY),
            ("", 6, CARD_BG),
            ("Regulators", 14, WHITE, True),
            ("\u2022 FTC, SEC, Nevada Gaming Board", 11, LIGHT_GRAY),
        ],
        line_spacing=1.4)

    add_slide_number(slide, 4, total_slides)

    # =========================================================================
    # SLIDE 5: What Happened - Timeline
    # =========================================================================
    slide = prs.slides.add_slide(slide_layout)
    set_slide_bg(slide, SLIDE_BG_DARK)
    add_accent_line(slide, Inches(0), Inches(0), SLIDE_WIDTH, ACCENT_BLUE)

    add_text_box(slide, Inches(0.8), Inches(0.5), Inches(3), Inches(0.5),
                 "03  TIMELINE", font_size=14, color=ACCENT_BLUE, bold=True)
    add_text_box(slide, Inches(0.8), Inches(1.0), Inches(10), Inches(0.7),
                 "What Happened \u2014 Attack Timeline", font_size=32, color=WHITE, bold=True)
    add_accent_line(slide, Inches(0.8), Inches(1.8), Inches(2.5), ACCENT_BLUE)

    timeline = [
        ("SEP 7", "Initial Breach via Social Engineering",
         "Scattered Spider impersonated an MGM employee using LinkedIn "
         "information. A 10-minute vishing call to the IT help desk "
         "resulted in MFA reset and credential access."),
        ("SEP 8\u20139", "Privilege Escalation & Lateral Movement",
         "Attackers gained admin access to Okta and Azure environments. "
         "Configured rogue Identity Provider using Okta's inbound federation. "
         "Began password sniffing on authentication servers."),
        ("SEP 10", "Systems Begin Failing",
         "MGM experiences system outages across properties. Slot machines, "
         "digital keys, reservations, and POS systems start going offline."),
        ("SEP 11", "Public Disclosure",
         "MGM publicly discloses the \"cybersecurity incident\" on X (Twitter) "
         "and contacts law enforcement. FBI investigation begins."),
        ("SEP 12\u201314", "Ransomware Deployed & Data Exfiltrated",
         "ALPHV deploys ransomware to 100+ ESXi hypervisors. Claims to "
         "exfiltrate 6 TB of customer data. Demands $30M ransom."),
        ("SEP 20", "Systems Restored",
         "MGM confirms full restoration of all guest-facing systems. "
         "MGM refuses to pay the ransom demand."),
    ]

    y_start = Inches(2.3)
    for i, (date, title, desc) in enumerate(timeline):
        y = y_start + i * Inches(0.82)

        add_shape_with_fill(slide, Inches(0.8), y, Inches(1.1), Inches(0.65),
                            ACCENT_RED if i in [0, 4] else CARD_BG, corner_radius=0.08)
        add_text_box(slide, Inches(0.85), y + Inches(0.12), Inches(1.0), Inches(0.4),
                     date, font_size=11, color=WHITE, bold=True, alignment=PP_ALIGN.CENTER)

        add_text_box(slide, Inches(2.1), y + Inches(0.02), Inches(4), Inches(0.35),
                     title, font_size=13, color=WHITE, bold=True)
        add_text_box(slide, Inches(2.1), y + Inches(0.32), Inches(10), Inches(0.45),
                     desc, font_size=10.5, color=LIGHT_GRAY, line_spacing=1.3)

    add_slide_number(slide, 5, total_slides)

    # =========================================================================
    # SLIDE 6: Attack Flow (Technical)
    # =========================================================================
    slide = prs.slides.add_slide(slide_layout)
    set_slide_bg(slide, SLIDE_BG_DARK)
    add_accent_line(slide, Inches(0), Inches(0), SLIDE_WIDTH, ACCENT_BLUE)

    add_text_box(slide, Inches(0.8), Inches(0.5), Inches(3), Inches(0.5),
                 "04  ATTACK FLOW", font_size=14, color=ACCENT_BLUE, bold=True)
    add_text_box(slide, Inches(0.8), Inches(1.0), Inches(10), Inches(0.7),
                 "Technical Breakdown of the Cyberattack", font_size=32, color=WHITE, bold=True)
    add_accent_line(slide, Inches(0.8), Inches(1.8), Inches(2.5), ACCENT_BLUE)

    steps = [
        ("1", "RECONNAISSANCE", ACCENT_BLUE,
         ["Researched MGM employees on LinkedIn",
          "Identified high-privilege IT staff",
          "Gathered personal details for impersonation",
          "Leveraged credentials from prior data breaches"]),
        ("2", "INITIAL ACCESS", ACCENT_GOLD,
         ["Vishing call to MGM IT help desk",
          "Impersonated targeted employee",
          "Convinced help desk to reset MFA",
          "10-minute call granted full access"]),
        ("3", "PERSISTENCE", ACCENT_RED,
         ["Configured rogue IdP via Okta inbound federation",
          "Gained admin access to Okta & Azure",
          "Deployed credential harvesting tools",
          "Created backdoors for continued access"]),
        ("4", "RANSOMWARE", RGBColor(0xAA, 0x00, 0xFF),
         ["ALPHV deployed BlackCat ransomware",
          "Encrypted 100+ ESXi hypervisors",
          "Exfiltrated 6 TB of customer data",
          "Demanded $30 million ransom payment"]),
    ]

    card_width = Inches(2.8)
    card_spacing = Inches(0.3)
    start_x = Inches(0.8)

    for i, (num, title, color, bullets) in enumerate(steps):
        x = start_x + i * (card_width + card_spacing)
        y = Inches(2.3)

        num_shape = add_shape_with_fill(slide, x, y, Inches(0.45), Inches(0.45),
                                         color, corner_radius=0.15)
        add_text_box(slide, x, y + Inches(0.02), Inches(0.45), Inches(0.45),
                     num, font_size=18, color=WHITE, bold=True, alignment=PP_ALIGN.CENTER)

        add_text_box(slide, x + Inches(0.55), y + Inches(0.05), Inches(2.2), Inches(0.4),
                     title, font_size=13, color=color, bold=True)

        card = add_shape_with_fill(slide, x, y + Inches(0.6), card_width, Inches(3.8),
                                    CARD_BG, corner_radius=0.04)

        add_bullet_list(slide, x + Inches(0.2), y + Inches(0.8), Inches(2.4), Inches(3.5),
                        bullets, font_size=11, color=LIGHT_GRAY, line_spacing=1.8)

    # Arrow indicators between cards
    for i in range(3):
        x = start_x + (i + 1) * (card_width + card_spacing) - card_spacing / 2
        add_text_box(slide, x - Inches(0.15), Inches(2.35), Inches(0.3), Inches(0.4),
                     "\u25B6", font_size=16, color=MEDIUM_GRAY, alignment=PP_ALIGN.CENTER)

    add_slide_number(slide, 6, total_slides)

    # =========================================================================
    # SLIDE 7: Why It Became Important
    # =========================================================================
    slide = prs.slides.add_slide(slide_layout)
    set_slide_bg(slide, SLIDE_BG_DARK)
    add_accent_line(slide, Inches(0), Inches(0), SLIDE_WIDTH, ACCENT_BLUE)

    add_text_box(slide, Inches(0.8), Inches(0.5), Inches(3), Inches(0.5),
                 "05  SIGNIFICANCE", font_size=14, color=ACCENT_BLUE, bold=True)
    add_text_box(slide, Inches(0.8), Inches(1.0), Inches(10), Inches(0.7),
                 "Why This Case Became Important", font_size=32, color=WHITE, bold=True)
    add_accent_line(slide, Inches(0.8), Inches(1.8), Inches(2.5), ACCENT_BLUE)

    reasons = [
        ("Scale of Disruption", ACCENT_RED,
         "One of the largest hospitality cyberattacks in history. "
         "29 properties across the United States were simultaneously affected, "
         "with core services paralyzed for 10 days."),
        ("Human Factor Vulnerability", ACCENT_GOLD,
         "Demonstrated that even billion-dollar enterprises can be compromised "
         "through a single 10-minute phone call. Social engineering bypassed "
         "all technical security controls."),
        ("Industry-Wide Impact", ACCENT_BLUE,
         "Occurred alongside the Caesars Entertainment attack (who paid ransom). "
         "Prompted industry-wide reassessment of identity management and "
         "help desk verification procedures."),
        ("Regulatory Attention", ACCENT_GREEN,
         "Triggered investigations by the FBI, FTC, SEC, and state regulators. "
         "Led to debates about corporate cybersecurity obligations and "
         "the adequacy of data protection frameworks."),
    ]

    for i, (title, color, desc) in enumerate(reasons):
        col = Inches(0.8) if i % 2 == 0 else Inches(6.8)
        row = Inches(2.3) if i < 2 else Inches(4.8)

        card = add_shape_with_fill(slide, col, row, Inches(5.7), Inches(2.1),
                                    CARD_BG, corner_radius=0.04)
        add_shape_with_fill(slide, col, row, Inches(0.12), Inches(2.1), color)

        add_text_box(slide, col + Inches(0.4), row + Inches(0.2), Inches(5), Inches(0.4),
                     title, font_size=16, color=color, bold=True)
        add_text_box(slide, col + Inches(0.4), row + Inches(0.7), Inches(5), Inches(1.2),
                     desc, font_size=12, color=LIGHT_GRAY, line_spacing=1.5)

    add_slide_number(slide, 7, total_slides)

    # =========================================================================
    # SLIDE 8: Cybersecurity, Privacy & Ethical Issues
    # =========================================================================
    slide = prs.slides.add_slide(slide_layout)
    set_slide_bg(slide, SLIDE_BG_DARK)
    add_accent_line(slide, Inches(0), Inches(0), SLIDE_WIDTH, ACCENT_BLUE)

    add_text_box(slide, Inches(0.8), Inches(0.5), Inches(5), Inches(0.5),
                 "06  ISSUES ANALYSIS", font_size=14, color=ACCENT_BLUE, bold=True)
    add_text_box(slide, Inches(0.8), Inches(1.0), Inches(10), Inches(0.7),
                 "Cybersecurity, Privacy & Ethical Issues", font_size=32, color=WHITE, bold=True)
    add_accent_line(slide, Inches(0.8), Inches(1.8), Inches(2.5), ACCENT_BLUE)

    issues = [
        ("SECURITY ISSUES", ACCENT_RED, [
            "Weak help desk identity verification procedures",
            "Insufficient monitoring of privileged account activity",
            "Over-reliance on Okta without hardening federation features",
            "Credential reuse from prior breaches not detected",
            "Inadequate network segmentation between cloud and on-prem",
        ]),
        ("PRIVACY ISSUES", ACCENT_GOLD, [
            "6 TB of customer PII exfiltrated (names, DOB, IDs)",
            "Social Security and passport numbers compromised",
            "Customer data allegedly stored unencrypted",
            "Pre-2019 customer records remained accessible",
            "Millions of guest records exposed to criminal actors",
        ]),
        ("ETHICAL ISSUES", ACCENT_GREEN, [
            "Attackers exploited human trust and social goodwill",
            "Help desk workers manipulated through impersonation",
            "Criminal use of publicly available LinkedIn data",
            "Ransom extortion threatening public data release",
            "Debate over MGM's duty of care to customers",
        ]),
    ]

    for i, (title, color, bullets) in enumerate(issues):
        x = Inches(0.8) + i * Inches(4.1)
        card = add_shape_with_fill(slide, x, Inches(2.3), Inches(3.8), Inches(4.5),
                                    CARD_BG, corner_radius=0.04)
        add_shape_with_fill(slide, x, Inches(2.3), Inches(3.8), Inches(0.5), color)
        add_text_box(slide, x + Inches(0.3), Inches(2.35), Inches(3.2), Inches(0.4),
                     title, font_size=13, color=WHITE, bold=True)
        add_bullet_list(slide, x + Inches(0.3), Inches(3.0), Inches(3.3), Inches(3.5),
                        bullets, font_size=11, color=LIGHT_GRAY, line_spacing=1.8)

    add_slide_number(slide, 8, total_slides)

    # =========================================================================
    # SLIDE 9: Violated Rules, Laws & Policies
    # =========================================================================
    slide = prs.slides.add_slide(slide_layout)
    set_slide_bg(slide, SLIDE_BG_DARK)
    add_accent_line(slide, Inches(0), Inches(0), SLIDE_WIDTH, ACCENT_BLUE)

    add_text_box(slide, Inches(0.8), Inches(0.5), Inches(5), Inches(0.5),
                 "07  LEGAL FRAMEWORK", font_size=14, color=ACCENT_BLUE, bold=True)
    add_text_box(slide, Inches(0.8), Inches(1.0), Inches(10), Inches(0.7),
                 "Rules, Laws & Professional Principles Violated", font_size=32, color=WHITE, bold=True)
    add_accent_line(slide, Inches(0.8), Inches(1.8), Inches(2.5), ACCENT_BLUE)

    laws = [
        ("Computer Fraud and Abuse Act (CFAA)", ACCENT_RED,
         "Federal law prohibiting unauthorized access to protected computers. "
         "Scattered Spider's intrusion constitutes a direct violation, "
         "carrying criminal penalties of up to 20 years imprisonment."),
        ("State Data Breach Notification Laws", ACCENT_GOLD,
         "Nevada and other state laws require timely notification of affected "
         "individuals. MGM was obligated to disclose the breach and provide "
         "credit monitoring services to impacted customers."),
        ("SEC Disclosure Requirements", ACCENT_BLUE,
         "As a publicly traded company, MGM was required to disclose material "
         "cybersecurity incidents via SEC filings (Form 8-K). The $100M impact "
         "met materiality thresholds for mandatory reporting."),
        ("Industry Security Standards (PCI DSS)", ACCENT_GREEN,
         "Payment Card Industry Data Security Standards require robust access "
         "controls and encryption. The breach exposed potential gaps in MGM's "
         "compliance with these standards."),
        ("NIST Cybersecurity Framework", RGBColor(0xAA, 0x00, 0xFF),
         "The incident highlighted failures across multiple NIST pillars: "
         "Identify (risk assessment), Protect (access controls), and "
         "Detect (monitoring and anomaly detection)."),
        ("Professional Ethics / Duty of Care", RGBColor(0xFF, 0x66, 0x00),
         "MGM had a professional and ethical obligation to safeguard customer "
         "data. Lawsuits allege the company was warned by Okta about social "
         "engineering risks but failed to act adequately."),
    ]

    for i, (title, color, desc) in enumerate(laws):
        col = Inches(0.8) if i % 2 == 0 else Inches(6.8)
        row = Inches(2.2) + (i // 2) * Inches(1.7)

        card = add_shape_with_fill(slide, col, row, Inches(5.8), Inches(1.45),
                                    CARD_BG, corner_radius=0.04)
        add_shape_with_fill(slide, col, row, Inches(0.1), Inches(1.45), color)

        add_text_box(slide, col + Inches(0.3), row + Inches(0.1), Inches(5.2), Inches(0.35),
                     title, font_size=13, color=color, bold=True)
        add_text_box(slide, col + Inches(0.3), row + Inches(0.5), Inches(5.2), Inches(0.85),
                     desc, font_size=10.5, color=LIGHT_GRAY, line_spacing=1.4)

    add_slide_number(slide, 9, total_slides)

    # =========================================================================
    # SLIDE 10: Consequences & Impact
    # =========================================================================
    slide = prs.slides.add_slide(slide_layout)
    set_slide_bg(slide, SLIDE_BG_DARK)
    add_accent_line(slide, Inches(0), Inches(0), SLIDE_WIDTH, ACCENT_BLUE)

    add_text_box(slide, Inches(0.8), Inches(0.5), Inches(5), Inches(0.5),
                 "08  CONSEQUENCES", font_size=14, color=ACCENT_BLUE, bold=True)
    add_text_box(slide, Inches(0.8), Inches(1.0), Inches(10), Inches(0.7),
                 "Consequences & Impact", font_size=32, color=WHITE, bold=True)
    add_accent_line(slide, Inches(0.8), Inches(1.8), Inches(2.5), ACCENT_BLUE)

    # Stats row
    stats = [
        ("$100M", "Q3 Revenue Loss", ACCENT_RED),
        ("$10M", "Remediation Costs", ACCENT_GOLD),
        ("$45M", "Class-Action Settlement", ACCENT_BLUE),
        ("$40M+", "IT Infrastructure Upgrades", ACCENT_GREEN),
    ]

    for i, (val, label, color) in enumerate(stats):
        x = Inches(0.8) + i * Inches(3.1)
        card = add_shape_with_fill(slide, x, Inches(2.3), Inches(2.8), Inches(1.2),
                                    CARD_BG, corner_radius=0.04)
        add_text_box(slide, x, Inches(2.4), Inches(2.8), Inches(0.6),
                     val, font_size=28, color=color, bold=True, alignment=PP_ALIGN.CENTER)
        add_text_box(slide, x, Inches(2.95), Inches(2.8), Inches(0.4),
                     label, font_size=11, color=LIGHT_GRAY, alignment=PP_ALIGN.CENTER)

    # Impact categories
    impacts = [
        ("OPERATIONAL IMPACT", ACCENT_RED, [
            "10-day shutdown of slot machines, ATMs, POS systems",
            "Digital room keys and check-in systems disabled",
            "Website, mobile app, and booking systems offline",
            "Manual workarounds caused long guest queues",
            "Hotel occupancy dropped to 88% in September",
        ]),
        ("LEGAL & REGULATORY IMPACT", ACCENT_GOLD, [
            "Multiple class-action lawsuits filed",
            "$45 million settlement (2019 + 2023 breaches)",
            "FTC investigation into data security practices",
            "SEC filings required for material disclosure",
            "State-level investigations (Nevada Gaming Board)",
        ]),
        ("REPUTATIONAL & SOCIETAL IMPACT", ACCENT_BLUE, [
            "Loss of customer trust and confidence",
            "Heightened public awareness of social engineering",
            "Industry-wide reassessment of help desk security",
            "Debate on corporate cybersecurity responsibility",
            "Highlighted vulnerability of critical infrastructure",
        ]),
    ]

    for i, (title, color, bullets) in enumerate(impacts):
        x = Inches(0.8) + i * Inches(4.1)
        card = add_shape_with_fill(slide, x, Inches(3.8), Inches(3.8), Inches(3.3),
                                    CARD_BG, corner_radius=0.04)
        add_text_box(slide, x + Inches(0.3), Inches(3.95), Inches(3.3), Inches(0.35),
                     title, font_size=11, color=color, bold=True)
        add_accent_line(slide, x + Inches(0.3), Inches(4.3), Inches(1.2), color)
        add_bullet_list(slide, x + Inches(0.3), Inches(4.5), Inches(3.3), Inches(2.5),
                        bullets, font_size=10.5, color=LIGHT_GRAY, line_spacing=1.7)

    add_slide_number(slide, 10, total_slides)

    # =========================================================================
    # SLIDE 11: Personal Opinion
    # =========================================================================
    slide = prs.slides.add_slide(slide_layout)
    set_slide_bg(slide, SLIDE_BG_DARK)
    add_accent_line(slide, Inches(0), Inches(0), SLIDE_WIDTH, ACCENT_BLUE)

    add_text_box(slide, Inches(0.8), Inches(0.5), Inches(5), Inches(0.5),
                 "09  PERSONAL OPINION", font_size=14, color=ACCENT_BLUE, bold=True)
    add_text_box(slide, Inches(0.8), Inches(1.0), Inches(10), Inches(0.7),
                 "My Analysis & Opinion", font_size=32, color=WHITE, bold=True)
    add_accent_line(slide, Inches(0.8), Inches(1.8), Inches(2.5), ACCENT_BLUE)

    opinions = [
        ("The Human Element is the Greatest Vulnerability",
         "This case powerfully demonstrates that cybersecurity is not solely a technical "
         "challenge. A 10-minute phone call defeated millions of dollars in security infrastructure. "
         "Organizations must invest equally in human-factor security \u2014 training employees to "
         "recognize and resist social engineering \u2014 as they do in firewalls and encryption."),

        ("MGM's Decision Not to Pay Ransom Was Correct",
         "While the $100 million loss is staggering, MGM's refusal to pay the ransom was "
         "the ethical and strategically sound choice. Paying ransoms funds criminal enterprises "
         "and offers no guarantee of data recovery. MGM's stance, supported by the FBI, "
         "sends a critical message to future attackers. Caesars' decision to pay, in contrast, "
         "may have emboldened the same threat actors."),

        ("Shared Responsibility in the Digital Age",
         "The attack reveals a broader systemic issue: the tension between operational "
         "convenience and security. Okta's inbound federation feature was designed for "
         "legitimate business purposes but became a weapon. Both technology vendors and "
         "enterprises share responsibility for secure-by-default configurations."),

        ("Corporate Accountability Must Be Strengthened",
         "The fact that MGM had been warned by Okta about social engineering risks \u2014 "
         "and had suffered a previous breach in 2019 \u2014 yet still fell victim, suggests "
         "that current regulatory frameworks are insufficient to compel adequate investment "
         "in cybersecurity. Stronger enforcement and mandatory security standards are needed."),
    ]

    for i, (title, desc) in enumerate(opinions):
        col = Inches(0.8) if i % 2 == 0 else Inches(6.8)
        row = Inches(2.2) + (i // 2) * Inches(2.5)

        card = add_shape_with_fill(slide, col, row, Inches(5.8), Inches(2.2),
                                    CARD_BG, corner_radius=0.04)
        colors = [ACCENT_RED, ACCENT_GOLD, ACCENT_BLUE, ACCENT_GREEN]
        add_shape_with_fill(slide, col, row, Inches(0.1), Inches(2.2), colors[i])

        add_text_box(slide, col + Inches(0.35), row + Inches(0.15), Inches(5.2), Inches(0.35),
                     title, font_size=14, color=colors[i], bold=True)
        add_text_box(slide, col + Inches(0.35), row + Inches(0.55), Inches(5.1), Inches(1.5),
                     desc, font_size=11, color=LIGHT_GRAY, line_spacing=1.5)

    add_slide_number(slide, 11, total_slides)

    # =========================================================================
    # SLIDE 12: Lessons Learned & Recommendations + References
    # =========================================================================
    slide = prs.slides.add_slide(slide_layout)
    set_slide_bg(slide, SLIDE_BG_DARK)
    add_accent_line(slide, Inches(0), Inches(0), SLIDE_WIDTH, ACCENT_BLUE)

    add_text_box(slide, Inches(0.8), Inches(0.5), Inches(5), Inches(0.5),
                 "10  LESSONS & REFERENCES", font_size=14, color=ACCENT_BLUE, bold=True)
    add_text_box(slide, Inches(0.8), Inches(1.0), Inches(10), Inches(0.7),
                 "Lessons Learned & Recommendations", font_size=32, color=WHITE, bold=True)
    add_accent_line(slide, Inches(0.8), Inches(1.8), Inches(2.5), ACCENT_BLUE)

    # Recommendations
    recs = [
        ("Strengthen Identity Verification",
         "Implement multi-step verification for help desk requests. "
         "Never reset MFA based on a phone call alone. Use callback "
         "procedures and manager approval for privileged account changes."),
        ("Invest in Security Awareness Training",
         "Conduct regular social engineering simulations. Train all staff "
         "\u2014 especially help desk personnel \u2014 to recognize vishing, "
         "impersonation, and pretexting attacks."),
        ("Adopt Zero-Trust Architecture",
         "Eliminate implicit trust. Verify every access request regardless "
         "of source. Implement least-privilege access controls and "
         "continuous authentication across all systems."),
        ("Harden IAM and Cloud Configurations",
         "Restrict features like Okta inbound federation. Monitor for "
         "unauthorized IdP configurations. Segment cloud and on-premises "
         "environments to limit lateral movement."),
        ("Develop and Test Incident Response Plans",
         "Regularly rehearse cyberattack scenarios. Ensure rapid containment "
         "procedures are documented, and that response teams can isolate "
         "compromised systems without causing complete shutdowns."),
    ]

    for i, (title, desc) in enumerate(recs):
        y = Inches(2.2) + i * Inches(0.95)
        colors_list = [ACCENT_RED, ACCENT_GOLD, ACCENT_BLUE, ACCENT_GREEN,
                       RGBColor(0xAA, 0x00, 0xFF)]

        num_shape = add_shape_with_fill(slide, Inches(0.8), y, Inches(0.35), Inches(0.35),
                                         colors_list[i], corner_radius=0.15)
        add_text_box(slide, Inches(0.8), y + Inches(0.01), Inches(0.35), Inches(0.35),
                     str(i + 1), font_size=13, color=WHITE, bold=True, alignment=PP_ALIGN.CENTER)

        add_text_box(slide, Inches(1.3), y, Inches(4.5), Inches(0.3),
                     title, font_size=13, color=colors_list[i], bold=True)
        add_text_box(slide, Inches(1.3), y + Inches(0.3), Inches(5.5), Inches(0.6),
                     desc, font_size=10, color=LIGHT_GRAY, line_spacing=1.4)

    # Voice recording note
    voice_card = add_shape_with_fill(slide, Inches(0.8), Inches(6.95), Inches(6.5), Inches(0.4),
                                      ACCENT_RED, corner_radius=0.06)
    add_text_box(slide, Inches(1.0), Inches(6.97), Inches(6), Inches(0.35),
                 "\u2022  Student voice recording of recommendations should be added to this slide",
                 font_size=11, color=WHITE, bold=True)

    # References
    ref_card = add_shape_with_fill(slide, Inches(7.5), Inches(2.2), Inches(5.2), Inches(5.1),
                                    CARD_BG, corner_radius=0.04)
    add_text_box(slide, Inches(7.8), Inches(2.35), Inches(4.5), Inches(0.35),
                 "REFERENCES", font_size=12, color=ACCENT_BLUE, bold=True)
    add_accent_line(slide, Inches(7.8), Inches(2.7), Inches(1.2), ACCENT_BLUE)

    references = [
        "CyberArk. (2023). \"The MGM Resorts Attack: Initial Analysis.\" cyberark.com",
        "Netwrix. (2023). \"An Overview of the MGM Cyber Attack.\" netwrix.com",
        "Reuters. (2023). \"Casino giant MGM expects $100 million hit from hack.\" reuters.com",
        "BleepingComputer. (2023). \"MGM Resorts ransomware attack led to $100M loss.\" bleepingcomputer.com",
        "Cyberbit. (2023). \"The Scattered Spider MGM Cyberattack.\" cyberbit.com",
        "ClassAction.org. (2025). \"MGM Facing Class Action Over Cyberattack.\" classaction.org",
        "UH West Oahu. (2023). \"ALPHV: Hackers Reveal Details of MGM Cyber Attack.\" westoahu.hawaii.edu",
        "Las Vegas Review-Journal. (2025). \"MGM settles suit for $45M.\" reviewjournal.com",
    ]

    add_multiline_text(slide, Inches(7.8), Inches(2.85), Inches(4.7), Inches(4.3),
                       [(ref, 8.5, LIGHT_GRAY) for ref in references],
                       line_spacing=1.7)

    add_slide_number(slide, 12, total_slides)

    # Save
    output_path = "/workspace/MGM_Resorts_Cyberattack_2023_Amr_Omair.pptx"
    prs.save(output_path)
    print(f"Presentation saved to: {output_path}")
    return output_path


if __name__ == "__main__":
    create_presentation()
