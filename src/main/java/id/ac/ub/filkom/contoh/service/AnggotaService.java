package id.ac.ub.filkom.contoh.service;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import org.springframework.stereotype.Service;
import id.ac.ub.filkom.contoh.exception.AturanBisnisException;

import java.util.List;

/** Service: logika untuk fitur anggota. */
@Service
public class AnggotaService {

    private final AnggotaRepository anggotaRepository;

    public AnggotaService(AnggotaRepository anggotaRepository) {
        this.anggotaRepository = anggotaRepository;
    }

    public List<Anggota> lihatSemuaAnggota() {
        return anggotaRepository.findAll();
    }

    public Anggota tambahAnggota(Anggota anggota) {
        if (anggota.getIdAnggota() == null || anggota.getIdAnggota().isEmpty()
                || anggota.getNama() == null || anggota.getNama().isEmpty()) {
            throw new AturanBisnisException("ID dan nama anggota tidak boleh kosong");
        }
        if (anggota.getBatasPinjam() < 0) {
            throw new AturanBisnisException("Batas pinjam tidak boleh negatif");
        }
        if (anggotaRepository.existsById(anggota.getIdAnggota())) {
            throw new AturanBisnisException(
                    "ID anggota " + anggota.getIdAnggota() + " sudah dipakai");
        }
        return anggotaRepository.save(anggota);
    }

}