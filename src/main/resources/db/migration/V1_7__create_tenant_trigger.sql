CREATE OR REPLACE FUNCTION add_tenant_history()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO house_history (house_id, person_id, update_date, type)
    VALUES (NEW.house_id, NEW.id, current_timestamp, 'TENANT');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tenant_change_trigger
AFTER UPDATE OF house_id ON persons
FOR EACH ROW
EXECUTE FUNCTION add_tenant_history();
