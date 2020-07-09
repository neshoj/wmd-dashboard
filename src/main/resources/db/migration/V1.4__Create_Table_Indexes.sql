set foreign_key_checks = 0;

-- ----------------------
-- Weighbridge Transaction Table
-- ----------------------
CREATE INDEX ticket_no
ON weighing_transactions (ticket_no);

-- ----------------------
-- Tagging Transaction Table
-- ----------------------
CREATE INDEX tag_reference
ON tagging_transactions (tag_reference);

set foreign_key_checks = 1;