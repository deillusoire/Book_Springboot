package id.ac.ub.filkom.contoh.entity;

import id.ac.ub.filkom.contoh.exception.AturanBisnisException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "peminjaman")
public class Peminjaman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_buku")
    private Buku buku;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_anggota")
    private Anggota anggota;

    @Column(name = "tgl_pinjam", nullable = false)
    private LocalDate tglPinjam;

    @Column(name = "tgl_jatuh_tempo", nullable = false)
    private LocalDate tglJatuhTempo;

    @Column(name = "tgl_kembali")
    private LocalDate tglKembali;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private StatusPinjam status;

    protected Peminjaman() { }         // wajib ada untuk JPA

    public Peminjaman(Buku buku, Anggota anggota,
                      LocalDate tglPinjam, LocalDate tglJatuhTempo) {
        this.buku = buku;
        this.anggota = anggota;
        this.tglPinjam = tglPinjam;
        this.tglJatuhTempo = tglJatuhTempo;
        this.status = StatusPinjam.DIPINJAM;
    }

    /** Menandai buku sudah dikembalikan. Ditolak jika sudah pernah dikembalikan. */
    public void kembalikan() {
        if (status == StatusPinjam.DIKEMBALIKAN) {
            throw new AturanBisnisException("Buku ini sudah dikembalikan");
        }
        this.tglKembali = LocalDate.now();
        this.status = StatusPinjam.DIKEMBALIKAN;
    }

    public boolean isTerlambat() {
        return status == StatusPinjam.DIPINJAM
                && LocalDate.now().isAfter(tglJatuhTempo);
    }

    public Long getId()                 { return id; }
    public Buku getBuku()               { return buku; }
    public Anggota getAnggota()         { return anggota; }
    public LocalDate getTglPinjam()     { return tglPinjam; }
    public LocalDate getTglJatuhTempo() { return tglJatuhTempo; }
    public LocalDate getTglKembali()    { return tglKembali; }
    public StatusPinjam getStatus()     { return status; }
}