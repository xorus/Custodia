import org.json.JSONObject;

public class ParseJson {
	private JSONObject obj;
	public ParseJson(JSONObject o){
		this.obj=new JSONObject(o);
	}
	public int getX() {
		return this.obj.getInt('x');
	}
	public int getY() {
		return this.obj.getInt('y');
	}
	public int getAngle() {
		return this.obj.getInt('angle');
	}
	
}
