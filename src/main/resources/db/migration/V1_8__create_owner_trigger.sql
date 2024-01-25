CREATE OR REPLACE FUNCTION add_owner_history()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO house_history (house_id, person_id, update_date, type)
    VALUES (NEW.house_id, NEW.owner_id, current_timestamp, 'OWNER');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER owner_change_trigger
AFTER INSERT OR UPDATE OF owner_id ON house_owner
FOR EACH ROW
EXECUTE FUNCTION add_owner_history();
