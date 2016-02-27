package yan.campuscrise;

public class ClientTicketOrder {
	private String ClientOrderNo;
	private TicketItem[] TicketItems;

	public ClientTicketOrder() {
	}

	public String getClientOrderNo() {
		return ClientOrderNo;
	}

	public void setClientOrderNo(String clientOrderNo) {
		ClientOrderNo = clientOrderNo;
	}

	public TicketItem[] getTicketItems() {
		return TicketItems;
	}

	public void setTicketItems(TicketItem[] ticketItems) {
		TicketItems = ticketItems;
	}
}