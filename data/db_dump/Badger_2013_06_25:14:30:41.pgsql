--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: elswob
--

SELECT pg_catalog.setval('hibernate_sequence', 3, true);


--
-- Data for Name: meta_data; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY meta_data (id, version, description, genus, image_file, image_source, species) FROM stdin;
\.


--
-- Data for Name: genome_data; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY genome_data (id, version, date_string, gbrowse, gversion, meta_id) FROM stdin;
\.


--
-- Data for Name: file_data; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY file_data (id, version, blast, cov, description, download, file_dir, file_link, file_name, file_type, file_version, genome_id, loaded, search, source) FROM stdin;
\.


--
-- Data for Name: anno_data; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY anno_data (id, version, anno_file, filedata_id, link, loaded, regex, source, type) FROM stdin;
\.


--
-- Data for Name: gene_info; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY gene_info (id, version, contig_id, file_id, gc, gene_id, mrna_id, nuc, pep, source, start, stop, strand) FROM stdin;
\.


--
-- Data for Name: exon_info; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY exon_info (id, version, exon_number, gc, gene_id, phase, score, sequence, start, stop, strand) FROM stdin;
\.


--
-- Data for Name: ext_info; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY ext_info (id, version, ext_id, ext_source, link, regex) FROM stdin;
\.


--
-- Data for Name: gene_anno; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY gene_anno (id, version, anno_db, anno_id, anno_start, anno_stop, descr, gene_id, score, textsearchable_index_col) FROM stdin;
\.


--
-- Data for Name: gene_blast; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY gene_blast (id, version, align, anno_db, anno_id, anno_start, anno_stop, descr, gaps, gene_id, hit_start, hit_stop, hseq, identity, midline, positive, qseq, score, textsearchable_index_col) FROM stdin;
\.


--
-- Data for Name: gene_interpro; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY gene_interpro (id, version, anno_db, anno_id, anno_start, anno_stop, descr, gene_id, score, textsearchable_index_col) FROM stdin;
\.


--
-- Data for Name: genome_info; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY genome_info (id, version, contig_id, coverage, file_id, gc, length, non_atgc, sequence) FROM stdin;
\.


--
-- Data for Name: news; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY news (id, version, data_string, date_string, title_string) FROM stdin;
\.


--
-- Data for Name: ortho; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY ortho (id, version, gene_id, group_id, size, trans_name) FROM stdin;
\.


--
-- Data for Name: page_edits; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY page_edits (id, version, date_string, edit, page) FROM stdin;
\.


--
-- Data for Name: publication; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY publication (id, version, abstract_text, authors, date_string, doi, issue, journal, journal_short, meta_id, pubmed_id, title, volume, textsearchable_index_col) FROM stdin;
\.


--
-- Data for Name: registration_code; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY registration_code (id, date_created, token, username) FROM stdin;
\.


--
-- Data for Name: sec_role; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY sec_role (id, version, authority) FROM stdin;
1	0	ROLE_USER
2	0	ROLE_ADMIN
\.


--
-- Data for Name: sec_user; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY sec_user (id, version, account_expired, account_locked, enabled, password, password_expired, username) FROM stdin;
3	0	f	f	t	ff1b4a27562d8ffc821b4d7368818ad7c759cfc2068b7adf0d2712315d67359a	f	admin
\.


--
-- Data for Name: sec_user_sec_role; Type: TABLE DATA; Schema: public; Owner: elswob
--

COPY sec_user_sec_role (sec_role_id, sec_user_id) FROM stdin;
2	3
\.


--
-- PostgreSQL database dump complete
--

