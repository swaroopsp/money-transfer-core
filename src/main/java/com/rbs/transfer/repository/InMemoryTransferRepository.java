package com.rbs.transfer.repository;

import com.rbs.transfer.domain.Transfer;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class InMemoryTransferRepository implements TransferRepository {

	private final Queue<Transfer> transfers = new ConcurrentLinkedQueue<>();

	@Override
	public void add(Transfer transfer) {
		transfers.add(transfer);
	}

}
