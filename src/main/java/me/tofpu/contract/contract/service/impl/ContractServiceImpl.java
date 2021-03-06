package me.tofpu.contract.contract.service.impl;

import com.google.common.collect.Lists;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.data.DataManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
        for (final Contract contract : this.contracts) {
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
        for (final Contract contract : this.contracts) {
            if (contract.contractorId().equals(contractorId)) return Optional.of(contract);
        }
        return Optional.empty();
    }

    /**
     * @param uniqueId the contractor/employer id
     *
     * @return the contracts made/accepted by unique id
     */
    @Override
    public List<Contract> of(final UUID uniqueId) {
        final List<Contract> contracts = Lists.newArrayList();
        for (final Contract contract : this.contracts) {
            if (contract.contractorId().equals(uniqueId) || contract.employerId().equals(uniqueId))
                contracts.add(contract);
        }
        return contracts;
    }

    /**
     * @param contractId the contract id
     *
     * @return an instance of contract id, if it's available
     */
    @Override
    public Optional<Contract> getContractById(final UUID contractId) {
        for (final Contract contract : this.contracts) {
            if (contract.id().equals(contractId)) return Optional.of(contract);
        }
        return Optional.empty();
    }

    /**
     * Loads all the contracts stored in the directory param
     *
     * @param directory the directory where all the contracts is stored at
     */
    @Override
    public void loadAll(final File directory) {
        if (!directory.exists()) return;
        final List<Contract> contracts = Lists.newArrayList();

        for (final File file : directory.listFiles()) {
            try (final FileReader reader = new FileReader(file)) {
                contracts.add(DataManager.GSON.fromJson(reader, Contract.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!contracts.isEmpty()) this.contracts.addAll(contracts);
    }

    /**
     * Saves all the contracts to that specific directory
     *
     * @param directory the directory to save all the contracts
     */
    @Override
    public void saveAll(final File directory) {
        for (final Contract contract : this.contracts) {
            if (contract == null || contract.id() == null) continue;
            final File file = new File(directory, contract.id() + ".json");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try (final FileWriter writer = new FileWriter(file)) {
                writer.write(DataManager.GSON.toJson(contract, Contract.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addContract(final Contract contract) {
        this.contracts.add(contract);
    }
}
