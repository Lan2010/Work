package core.component;

public interface NatsComponent {
	
	public void handleMessage();
	
	public void publish4oms(String subject,String msg);
	
}
