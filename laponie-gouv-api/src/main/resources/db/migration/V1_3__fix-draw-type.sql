ALTER TABLE family
    ALTER COLUMN draw type jsonb USING draw::jsonb;
