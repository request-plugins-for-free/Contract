package me.tofpu.contract.contract.service.impl;

import com.google.common.collect.Lists;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.service.ContractService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ContractServiceImpl implements ContractService {
    private final List<Contract> contracts;

    public ContractServiceImpl() {
        contracts = Lists.newArrayList();
    }

    /**
     * @param contract adds the contract to a list
     */
    @Override
    public void registerContract(final Contract contract) {
        addContract(contract);
    }

    /**
     * @param employerId the employer uniqueId (whom creates a contract)
     *
     * @return a list of available contracts that the employer contracted
     */
    @Override
    public List<Contract> getEmployerContracts(final UUID employerId) {
        final List<Contract> contracts = Lists.newArrayList();
        for (final Contract contract : this.contracts){
            if (contract.employerId().equals(employerId)) contracts.add(contract);
        }
        return contracts;
    }

    /**
     * @param contractorId the contractor uniqueId (whom accepts contracts)
     *
     * @return an available contract
     */
    @Override
    public Optional<Contract> getContractorContract(final UUID contractorId) {
        for (final Contract contract : this.contracts){
            if (contract.contractorId().equals(contractorId)) return Optional.of(contract);
        }
        return Optional.empty();
    }

    private void addContract(final Contract contract){
        this.contracts.add(contract);
    }
}
