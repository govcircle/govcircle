
INSERT INTO public.gc_role (code, created_at, created_by_id, id, updated_at, updated_by_id, description, ip, title) VALUES (101, null, null, 1, null, null, 'Stake Pool Operator', null, 'SPO');
INSERT INTO public.gc_role (code, created_at, created_by_id, id, updated_at, updated_by_id, description, ip, title) VALUES (102, null, null, 2, null, null, 'Constitution Committee', null, 'CC');
INSERT INTO public.gc_role (code, created_at, created_by_id, id, updated_at, updated_by_id, description, ip, title) VALUES (103, null, null, 3, null, null, 'Delegated Representatives', null, 'DRep');

INSERT INTO public.gc_authority (code, created_at, created_by_id, id, updated_at, updated_by_id, authoritytype, description, ip, title) VALUES (1001, null, null, 1, null, null, null, null, null, 'create proposal');

INSERT INTO public.gc_role_authority (end_slot, start_slot, authority_id, created_at, created_by_id, id, role_id, updated_at, updated_by_id, ip) VALUES (0, 0, 1, null, null, 1, 1, null, null, null);
INSERT INTO public.gc_role_authority (end_slot, start_slot, authority_id, created_at, created_by_id, id, role_id, updated_at, updated_by_id, ip) VALUES (0, 0, 1, null, null, 2, 2, null, null, null);
INSERT INTO public.gc_role_authority (end_slot, start_slot, authority_id, created_at, created_by_id, id, role_id, updated_at, updated_by_id, ip) VALUES (0, 0, 1, null, null, 3, 3, null, null, null);
