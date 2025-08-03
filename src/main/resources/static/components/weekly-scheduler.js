// Компонент для недельного планировщика смен
function WeeklyShiftScheduler({ users, shifts, groups, onSaveSchedule }) {
    const [selectedWeek, setSelectedWeek] = React.useState(getWeekStart(new Date()));
    const [scheduleData, setScheduleData] = React.useState([]);
    const [loading, setLoading] = React.useState(false);
    const [viewMode, setViewMode] = React.useState('users'); // 'users' или 'groups'

    // Получение начала недели
    function getWeekStart(date) {
        const d = new Date(date);
        const day = d.getDay();
        const diff = d.getDate() - day + (day === 0 ? -6 : 1);
        return new Date(d.setDate(diff)).toISOString().split('T')[0];
    }

    // Получение дней недели
    function getWeekDays(startDate) {
        const days = [];
        const start = new Date(startDate);
        for (let i = 0; i < 7; i++) {
            const day = new Date(start);
            day.setDate(start.getDate() + i);
            days.push({
                date: day.toISOString().split('T')[0],
                dayName: day.toLocaleDateString('ru-RU', { weekday: 'short' }),
                dayNumber: day.getDate()
            });
        }
        return days;
    }

    // Загрузка данных расписания
    React.useEffect(() => {
        if (selectedWeek) {
            loadScheduleData();
        }
    }, [selectedWeek]);

    const loadScheduleData = async () => {
        setLoading(true);
        try {
            const weekDays = getWeekDays(selectedWeek);
            const weekData = [];
            
            for (const day of weekDays) {
                const response = await fetch(`/api/attendance/date/${day.date}`);
                if (response.ok) {
                    const dayData = await response.json();
                    weekData.push({
                        date: day.date,
                        dayName: day.dayName,
                        dayNumber: day.dayNumber,
                        attendance: dayData
                    });
                }
            }
            setScheduleData(weekData);
        } catch (error) {
            console.error('Ошибка загрузки данных расписания:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleWeekChange = (e) => {
        setSelectedWeek(e.target.value);
    };

    const handleScheduleChange = (date, userId, field, value) => {
        setScheduleData(prev => 
            prev.map(day => 
                day.date === date 
                    ? {
                        ...day,
                        attendance: day.attendance.map(item => 
                            item.userId === userId 
                                ? { ...item, [field]: value }
                                : item
                        )
                    }
                    : day
            )
        );
    };

    const handleSave = async () => {
        try {
            for (const day of scheduleData) {
                for (const attendance of day.attendance) {
                    if (attendance.id) {
                        await fetch(`/api/attendance/${attendance.id}`, {
                            method: 'PUT',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify(attendance)
                        });
                    } else if (attendance.shiftId || attendance.isAbsent) {
                        await fetch('/api/attendance', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify(attendance)
                        });
                    }
                }
            }
            loadScheduleData();
        } catch (error) {
            console.error('Ошибка сохранения:', error);
        }
    };

    const weekDays = getWeekDays(selectedWeek);

    return (
        <div className="weekly-scheduler-container">
            <div className="scheduler-header">
                <h2>Недельный планировщик смен</h2>
                <div className="scheduler-controls">
                    <div className="week-selector">
                        <label>Неделя: </label>
                        <input 
                            type="date" 
                            value={selectedWeek} 
                            onChange={handleWeekChange}
                            className="form-control"
                        />
                    </div>
                    <div className="view-mode">
                        <label>
                            <input 
                                type="radio" 
                                name="viewMode" 
                                value="users" 
                                checked={viewMode === 'users'}
                                onChange={(e) => setViewMode(e.target.value)}
                            />
                            По сотрудникам
                        </label>
                        <label>
                            <input 
                                type="radio" 
                                name="viewMode" 
                                value="groups" 
                                checked={viewMode === 'groups'}
                                onChange={(e) => setViewMode(e.target.value)}
                            />
                            По группам
                        </label>
                    </div>
                    <button 
                        className="btn btn-primary" 
                        onClick={handleSave}
                        disabled={loading}
                    >
                        {loading ? 'Сохранение...' : 'Сохранить'}
                    </button>
                </div>
            </div>

            <div className="scheduler-table">
                <table className="table table-bordered">
                    <thead>
                        <tr>
                            <th>Сотрудник/Группа</th>
                            {weekDays.map(day => (
                                <th key={day.date} className="day-header">
                                    <div className="day-name">{day.dayName}</div>
                                    <div className="day-number">{day.dayNumber}</div>
                                </th>
                            ))}
                        </tr>
                    </thead>
                    <tbody>
                        {viewMode === 'users' ? (
                            // Вид по сотрудникам
                            users.map(user => (
                                <tr key={user.id}>
                                    <td className="user-name">{user.name}</td>
                                    {weekDays.map(day => {
                                        const dayAttendance = scheduleData.find(d => d.date === day.date)?.attendance.find(a => a.userId === user.id) || {
                                            userId: user.id,
                                            userName: user.name,
                                            date: day.date,
                                            shiftId: null,
                                            isAbsent: false,
                                            notes: ''
                                        };

                                        return (
                                            <td key={day.date} className="schedule-cell">
                                                <select 
                                                    className="form-control form-control-sm"
                                                    value={dayAttendance.shiftId || ''}
                                                    onChange={(e) => handleScheduleChange(day.date, user.id, 'shiftId', e.target.value ? parseInt(e.target.value) : null)}
                                                >
                                                    <option value="">-</option>
                                                    {shifts.map(shift => (
                                                        <option key={shift.id} value={shift.id}>
                                                            {shift.name}
                                                        </option>
                                                    ))}
                                                </select>
                                                <div className="absence-check">
                                                    <input 
                                                        type="checkbox" 
                                                        checked={dayAttendance.isAbsent || false}
                                                        onChange={(e) => handleScheduleChange(day.date, user.id, 'isAbsent', e.target.checked)}
                                                        title="Отсутствует"
                                                    />
                                                </div>
                                            </td>
                                        );
                                    })}
                                </tr>
                            ))
                        ) : (
                            // Вид по группам
                            groups.map(group => (
                                <tr key={group.id}>
                                    <td className="group-name">{group.name}</td>
                                    {weekDays.map(day => {
                                        const groupUsers = users.filter(user => user.groupId === group.id);
                                        const groupShifts = groupUsers.map(user => {
                                            const dayAttendance = scheduleData.find(d => d.date === day.date)?.attendance.find(a => a.userId === user.id);
                                            return dayAttendance?.shiftId ? shifts.find(s => s.id === dayAttendance.shiftId)?.name : null;
                                        }).filter(Boolean);

                                        return (
                                            <td key={day.date} className="schedule-cell group-cell">
                                                <div className="group-shifts">
                                                    {groupShifts.length > 0 ? (
                                                        groupShifts.map((shiftName, index) => (
                                                            <span key={index} className="shift-badge">{shiftName}</span>
                                                        ))
                                                    ) : (
                                                        <span className="no-shift">-</span>
                                                    )}
                                                </div>
                                            </td>
                                        );
                                    })}
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>

            <div className="scheduler-summary">
                <h3>Сводка недели</h3>
                <div className="summary-stats">
                    <div className="stat-item">
                        <strong>Всего смен:</strong> 
                        {scheduleData.reduce((sum, day) => 
                            sum + day.attendance.filter(a => a.shiftId).length, 0
                        )}
                    </div>
                    <div className="stat-item">
                        <strong>Всего отсутствий:</strong> 
                        {scheduleData.reduce((sum, day) => 
                            sum + day.attendance.filter(a => a.isAbsent).length, 0
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

// Экспорт для использования в основном файле
if (typeof module !== 'undefined' && module.exports) {
    module.exports = WeeklyShiftScheduler;
} 